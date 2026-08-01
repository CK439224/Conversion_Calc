package com.numconversion.app.domain.conversion

import com.numconversion.app.domain.fraction.FractionFormatter
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * Generic conversion engine for every [UnitCategory]. Each unit (other than [MeasurementUnit.FT_IN]
 * and the TEMPERATURE units) carries its own exact [MeasurementUnit.baseFactor], so converting is
 * just "multiply into the category's base unit, then divide into the target" — see
 * [MeasurementUnit] for what each category's base unit is and where its factors come from.
 * LENGTH keeps its two special cases from the original single-category engine: [MeasurementUnit.FT_IN]
 * (a composite feet+inches unit) and [MeasurementUnit.FRACTION] (formats as a reduced fraction of
 * an inch instead of a decimal). TEMPERATURE is affine, not multiplicative, so it's converted via
 * a dedicated Celsius-pivot path instead of [MeasurementUnit.baseFactor].
 */
object UnitConverter {

    val MM_PER_INCH: BigDecimal = BigDecimal("25.4")
    val INCHES_PER_FOOT: BigDecimal = BigDecimal(12)

    /** Nearest-1/64" is the standard trades/tape-measure display resolution. */
    const val FRACTION_DENOMINATOR = 64L

    private val COMPUTE_CONTEXT = MathContext(20, RoundingMode.HALF_UP)
    private const val DISPLAY_SCALE = 6

    /** Converts a plain numeric value in [unit] to its category's base unit. Not valid for
     *  FT_IN (composite; use [feetInchesToMillimeters]) or TEMPERATURE (affine; use [convert]). */
    fun toBaseUnit(value: BigDecimal, unit: MeasurementUnit): BigDecimal {
        require(unit != MeasurementUnit.FT_IN) {
            "FT_IN is a composite unit; use feetInchesToMillimeters"
        }
        require(unit.category != UnitCategory.TEMPERATURE) {
            "$unit is a temperature unit; use convert(), which handles the affine conversion"
        }
        val factor = requireNotNull(unit.baseFactor) { "$unit has no base factor" }
        return value.multiply(factor)
    }

    fun feetInchesToMillimeters(feet: BigDecimal, inches: BigDecimal): BigDecimal {
        val totalInches = feet.multiply(INCHES_PER_FOOT).add(inches)
        return totalInches.multiply(MM_PER_INCH)
    }

    /** Converts a value already in [targetUnit]'s category base unit into [targetUnit] and
     *  appends its abbreviation, e.g. "1 in", "5 ft 4 in". Not valid for TEMPERATURE targets. */
    fun formatConversion(
        baseValue: BigDecimal,
        targetUnit: MeasurementUnit,
        fractionDenominator: Long = FRACTION_DENOMINATOR
    ): String = when (targetUnit) {
        MeasurementUnit.FT_IN -> formatFeetInches(baseValue)
        MeasurementUnit.FRACTION -> "${formatFraction(baseValue, fractionDenominator)} in"
        else -> {
            val factor = requireNotNull(targetUnit.baseFactor) { "$targetUnit has no base factor" }
            "${plain(baseValue.divide(factor, COMPUTE_CONTEXT))} ${targetUnit.displayLabel}"
        }
    }

    fun convert(
        value: BigDecimal,
        fromUnit: MeasurementUnit,
        toUnit: MeasurementUnit,
        fractionDenominator: Long = FRACTION_DENOMINATOR
    ): String {
        require(fromUnit != MeasurementUnit.FT_IN) {
            "FT_IN source requires convertFromFeetInches(feet, inches, toUnit)"
        }
        if (fromUnit.category == UnitCategory.TEMPERATURE) {
            return formatTemperature(convertTemperature(value, fromUnit, toUnit), toUnit)
        }
        return formatConversion(toBaseUnit(value, fromUnit), toUnit, fractionDenominator)
    }

    fun convertFromFeetInches(
        feet: BigDecimal,
        inches: BigDecimal,
        toUnit: MeasurementUnit,
        fractionDenominator: Long = FRACTION_DENOMINATOR
    ): String {
        return formatConversion(feetInchesToMillimeters(feet, inches), toUnit, fractionDenominator)
    }

    /** Celsius-pivot conversion: °F = °C × 9/5 + 32, K = °C + 273.15. */
    private fun convertTemperature(value: BigDecimal, fromUnit: MeasurementUnit, toUnit: MeasurementUnit): BigDecimal {
        val celsius = when (fromUnit) {
            MeasurementUnit.CELSIUS -> value
            MeasurementUnit.FAHRENHEIT ->
                value.subtract(BigDecimal(32)).multiply(BigDecimal(5)).divide(BigDecimal(9), COMPUTE_CONTEXT)
            MeasurementUnit.KELVIN -> value.subtract(BigDecimal("273.15"))
            else -> throw IllegalArgumentException("$fromUnit is not a temperature unit")
        }
        return when (toUnit) {
            MeasurementUnit.CELSIUS -> celsius
            MeasurementUnit.FAHRENHEIT ->
                celsius.multiply(BigDecimal(9)).divide(BigDecimal(5), COMPUTE_CONTEXT).add(BigDecimal(32))
            MeasurementUnit.KELVIN -> celsius.add(BigDecimal("273.15"))
            else -> throw IllegalArgumentException("$toUnit is not a temperature unit")
        }
    }

    private fun formatTemperature(value: BigDecimal, unit: MeasurementUnit): String =
        "${plain(value)} ${unit.displayLabel}"

    private fun formatFeetInches(mm: BigDecimal): String {
        val totalInches = mm.divide(MM_PER_INCH, COMPUTE_CONTEXT)
        val feet = totalInches.divideToIntegralValue(INCHES_PER_FOOT)
        val remainderInches = totalInches.subtract(feet.multiply(INCHES_PER_FOOT))
        return "${plain(feet)} ft ${plain(remainderInches, scale = 2)} in"
    }

    private fun formatFraction(mm: BigDecimal, fractionDenominator: Long): String {
        val totalInches = mm.divide(MM_PER_INCH, COMPUTE_CONTEXT)
        val numerator = totalInches.multiply(BigDecimal(fractionDenominator))
            .setScale(0, RoundingMode.HALF_UP)
            .longValueExact()
        return FractionFormatter.format(numerator, fractionDenominator)
    }

    /** Rounds to [scale] decimal places for readability, then trims trailing zeros without scientific notation. */
    private fun plain(value: BigDecimal, scale: Int = DISPLAY_SCALE): String {
        val rounded = value.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros()
        return (if (rounded.scale() < 0) rounded.setScale(0) else rounded).toPlainString()
    }
}
