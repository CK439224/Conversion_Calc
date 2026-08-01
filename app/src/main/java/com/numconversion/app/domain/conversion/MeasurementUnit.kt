package com.numconversion.app.domain.conversion

import java.math.BigDecimal

/**
 * Every unit the converter supports, grouped by [category]. [baseFactor] is "how many of this
 * category's base unit equal 1 of this unit" (base units: mm for LENGTH, g for WEIGHT, mL for
 * VOLUME, m² for AREA, m/s for SPEED, s for TIME) — [UnitConverter] multiplies by it to convert
 * into the base and divides by it to convert out, so a plain multiplicative factor is all any
 * unit needs. [baseFactor] is null only for [FT_IN] (a composite feet+inches unit, converted via
 * [UnitConverter.feetInchesToMillimeters] instead) and the three TEMPERATURE units (Celsius,
 * Fahrenheit, and Kelvin relate by an affine, not multiplicative, formula).
 *
 * Every non-null factor below is an exact, internationally-defined constant (not an
 * approximation) — see the source noted on each group.
 */
enum class MeasurementUnit(
    val category: UnitCategory,
    val displayLabel: String,
    val baseFactor: BigDecimal?
) {
    // LENGTH — base unit: millimeter. 1 in = 25.4 mm exactly (International Yard and Pound
    // Agreement, 1959); yd/mi are exact multiples of the inch; mm/cm/m/km are exact SI multiples.
    FT_IN(UnitCategory.LENGTH, "Ft In", null),
    IN(UnitCategory.LENGTH, "in", BigDecimal("25.4")),
    FRACTION(UnitCategory.LENGTH, "Fraction", BigDecimal("25.4")),
    YD(UnitCategory.LENGTH, "yd", BigDecimal("914.4")),
    MI(UnitCategory.LENGTH, "mi", BigDecimal("1609344")),
    MM(UnitCategory.LENGTH, "mm", BigDecimal.ONE),
    CM(UnitCategory.LENGTH, "cm", BigDecimal("10")),
    M(UnitCategory.LENGTH, "m", BigDecimal("1000")),
    KM(UnitCategory.LENGTH, "km", BigDecimal("1000000")),

    // WEIGHT — base unit: gram. 1 lb = 453.59237 g exactly (same 1959 agreement); oz/stone/ton
    // are exact multiples of the pound; mg/kg/metric ton are exact SI multiples.
    OZ(UnitCategory.WEIGHT, "oz", BigDecimal("28.349523125")),
    LB(UnitCategory.WEIGHT, "lb", BigDecimal("453.59237")),
    STONE(UnitCategory.WEIGHT, "st", BigDecimal("6350.29318")),
    US_TON(UnitCategory.WEIGHT, "ton", BigDecimal("907184.74")),
    MG(UnitCategory.WEIGHT, "mg", BigDecimal("0.001")),
    G(UnitCategory.WEIGHT, "g", BigDecimal.ONE),
    KG(UnitCategory.WEIGHT, "kg", BigDecimal("1000")),
    METRIC_TON(UnitCategory.WEIGHT, "t", BigDecimal("1000000")),

    // VOLUME — base unit: milliliter. 1 US gallon = 3.785411784 L exactly (231 in³ exactly; NIST
    // Handbook 44); tsp/tbsp/fl oz/cup/pint/quart are exact fractions of the US gallon (US
    // customary only — Imperial/UK volumes are different sizes and deliberately omitted).
    TSP(UnitCategory.VOLUME, "tsp", BigDecimal("4.92892159375")),
    TBSP(UnitCategory.VOLUME, "tbsp", BigDecimal("14.78676478125")),
    FL_OZ(UnitCategory.VOLUME, "fl oz", BigDecimal("29.5735295625")),
    CUP(UnitCategory.VOLUME, "cup", BigDecimal("236.5882365")),
    PINT(UnitCategory.VOLUME, "pt", BigDecimal("473.176473")),
    QUART(UnitCategory.VOLUME, "qt", BigDecimal("946.352946")),
    GALLON(UnitCategory.VOLUME, "gal", BigDecimal("3785.411784")),
    ML(UnitCategory.VOLUME, "mL", BigDecimal.ONE),
    LITER(UnitCategory.VOLUME, "L", BigDecimal("1000")),
    CUBIC_METER(UnitCategory.VOLUME, "m³", BigDecimal("1000000")),

    // AREA — base unit: square meter. Each factor is the square of the corresponding exact
    // LENGTH factor (e.g. 1 ft = 0.3048 m exactly, so 1 ft² = 0.3048² = 0.09290304 m² exactly).
    // 1 acre = 43,560 ft² exactly (1 chain × 1 furlong).
    SQ_IN(UnitCategory.AREA, "in²", BigDecimal("0.00064516")),
    SQ_FT(UnitCategory.AREA, "ft²", BigDecimal("0.09290304")),
    SQ_YD(UnitCategory.AREA, "yd²", BigDecimal("0.83612736")),
    SQ_MI(UnitCategory.AREA, "mi²", BigDecimal("2589988.110336")),
    ACRE(UnitCategory.AREA, "acre", BigDecimal("4046.8564224")),
    SQ_MM(UnitCategory.AREA, "mm²", BigDecimal("0.000001")),
    SQ_CM(UnitCategory.AREA, "cm²", BigDecimal("0.0001")),
    SQ_M(UnitCategory.AREA, "m²", BigDecimal.ONE),
    SQ_KM(UnitCategory.AREA, "km²", BigDecimal("1000000")),
    HECTARE(UnitCategory.AREA, "ha", BigDecimal("10000")),

    // TEMPERATURE — not multiplicative; converted via UnitConverter's dedicated Celsius-pivot
    // formulas (°F = °C × 9/5 + 32, K = °C + 273.15), not baseFactor.
    CELSIUS(UnitCategory.TEMPERATURE, "°C", null),
    FAHRENHEIT(UnitCategory.TEMPERATURE, "°F", null),
    KELVIN(UnitCategory.TEMPERATURE, "K", null),

    // SPEED — base unit: meter/second. 1 mph = 1609.344/3600 = 0.44704 m/s exactly. km/h and knot
    // (1 knot = 1852 m/s ÷ 3600, international nautical mile) aren't terminating decimals, so
    // they're carried to 18 decimal places — far past this app's 6-decimal display rounding.
    MPH(UnitCategory.SPEED, "mph", BigDecimal("0.44704")),
    KNOT(UnitCategory.SPEED, "kn", BigDecimal("0.514444444444444444")),
    KMH(UnitCategory.SPEED, "km/h", BigDecimal("0.277777777777777778")),
    MPS(UnitCategory.SPEED, "m/s", BigDecimal.ONE),

    // TIME — base unit: second. All exact by definition.
    SECOND(UnitCategory.TIME, "s", BigDecimal.ONE),
    MINUTE(UnitCategory.TIME, "min", BigDecimal("60")),
    HOUR(UnitCategory.TIME, "hr", BigDecimal("3600")),
    DAY(UnitCategory.TIME, "day", BigDecimal("86400"));

    companion object {
        /** The sensible default source/target pair shown when the user switches to [category]. */
        fun defaultsFor(category: UnitCategory): Pair<MeasurementUnit, MeasurementUnit> = when (category) {
            UnitCategory.LENGTH -> MM to IN
            UnitCategory.WEIGHT -> KG to LB
            UnitCategory.VOLUME -> LITER to GALLON
            UnitCategory.AREA -> SQ_M to SQ_FT
            UnitCategory.TEMPERATURE -> CELSIUS to FAHRENHEIT
            UnitCategory.SPEED -> KMH to MPH
            UnitCategory.TIME -> MINUTE to HOUR
        }
    }
}
