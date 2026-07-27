package com.numconversion.app.domain.conversion

import com.numconversion.app.domain.fraction.FractionFormatter
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * Length conversion engine. Internal base unit is millimeters.
 *
 * Exact, internationally-defined constants (not approximations):
 *  - 1 in = 25.4 mm exactly (International Yard and Pound Agreement, 1959; NIST Handbook 44).
 *  - 1 ft = 12 in (definitional).
 *  - 1 m = 1000 mm (SI prefix, definitional).
 */
object UnitConverter {

    val MM_PER_INCH: BigDecimal = BigDecimal("25.4")
    val INCHES_PER_FOOT: BigDecimal = BigDecimal(12)
    val MM_PER_METER: BigDecimal = BigDecimal(1000)

    /** Nearest-1/64" is the standard trades/tape-measure display resolution. */
    const val FRACTION_DENOMINATOR = 64L

    private val COMPUTE_CONTEXT = MathContext(20, RoundingMode.HALF_UP)
    private const val DISPLAY_SCALE = 6

    /** Converts a plain numeric value in [unit] to millimeters. Not valid for FT_IN (composite unit). */
    fun toMillimeters(value: BigDecimal, unit: MeasurementUnit): BigDecimal = when (unit) {
        MeasurementUnit.MM -> value
        MeasurementUnit.M -> value.multiply(MM_PER_METER)
        MeasurementUnit.IN, MeasurementUnit.FRACTION -> value.multiply(MM_PER_INCH)
        MeasurementUnit.FT_IN -> throw IllegalArgumentException(
            "FT_IN is a composite unit; use toMillimetersFromFeetInches"
        )
    }

    fun toMillimetersFromFeetInches(feet: BigDecimal, inches: BigDecimal): BigDecimal {
        val totalInches = feet.multiply(INCHES_PER_FOOT).add(inches)
        return totalInches.multiply(MM_PER_INCH)
    }

    /** Converts millimeters into [unit] and appends its unit abbreviation, e.g. "1 in", "5 ft 4 in". */
    fun formatConversion(
        mm: BigDecimal,
        targetUnit: MeasurementUnit,
        fractionDenominator: Long = FRACTION_DENOMINATOR
    ): String = when (targetUnit) {
        MeasurementUnit.MM -> "${plain(mm)} mm"
        MeasurementUnit.M -> "${plain(mm.divide(MM_PER_METER, COMPUTE_CONTEXT))} m"
        MeasurementUnit.IN -> "${plain(mm.divide(MM_PER_INCH, COMPUTE_CONTEXT))} in"
        MeasurementUnit.FRACTION -> "${formatFraction(mm, fractionDenominator)} in"
        MeasurementUnit.FT_IN -> formatFeetInches(mm)
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
        return formatConversion(toMillimeters(value, fromUnit), toUnit, fractionDenominator)
    }

    fun convertFromFeetInches(
        feet: BigDecimal,
        inches: BigDecimal,
        toUnit: MeasurementUnit,
        fractionDenominator: Long = FRACTION_DENOMINATOR
    ): String {
        return formatConversion(toMillimetersFromFeetInches(feet, inches), toUnit, fractionDenominator)
    }

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
