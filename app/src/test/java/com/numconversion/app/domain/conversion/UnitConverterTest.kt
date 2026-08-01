package com.numconversion.app.domain.conversion

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class UnitConverterTest {

    @Test
    fun `directive example - 25point4 mm to in is 1 in`() {
        assertEquals(
            "1 in",
            UnitConverter.convert(BigDecimal("25.4"), MeasurementUnit.MM, MeasurementUnit.IN)
        )
    }

    @Test
    fun `1 inch to mm is exactly 25point4 mm`() {
        assertEquals(
            "25.4 mm",
            UnitConverter.convert(BigDecimal("1"), MeasurementUnit.IN, MeasurementUnit.MM)
        )
    }

    @Test
    fun `1000 mm to m is 1 m`() {
        assertEquals(
            "1 m",
            UnitConverter.convert(BigDecimal("1000"), MeasurementUnit.MM, MeasurementUnit.M)
        )
    }

    @Test
    fun `1 m to mm is 1000 mm`() {
        assertEquals(
            "1000 mm",
            UnitConverter.convert(BigDecimal("1"), MeasurementUnit.M, MeasurementUnit.MM)
        )
    }

    @Test
    fun `64 inches formats as 5 ft 4 in`() {
        assertEquals(
            "5 ft 4 in",
            UnitConverter.convert(BigDecimal("64"), MeasurementUnit.IN, MeasurementUnit.FT_IN)
        )
    }

    @Test
    fun `5 ft 4 in round-trips to 64 in`() {
        assertEquals(
            "64 in",
            UnitConverter.convertFromFeetInches(BigDecimal("5"), BigDecimal("4"), MeasurementUnit.IN)
        )
    }

    @Test
    fun `quarter inch rounds to nearest 64th as 1 slash 4`() {
        assertEquals(
            "1/4 in",
            UnitConverter.convert(BigDecimal("0.25"), MeasurementUnit.IN, MeasurementUnit.FRACTION)
        )
    }

    @Test
    fun `three quarters inch formats as 3 slash 4`() {
        assertEquals(
            "3/4 in",
            UnitConverter.convert(BigDecimal("0.75"), MeasurementUnit.IN, MeasurementUnit.FRACTION)
        )
    }

    @Test
    fun `whole inch as fraction target has no fraction part`() {
        assertEquals(
            "2 in",
            UnitConverter.convert(BigDecimal("2"), MeasurementUnit.IN, MeasurementUnit.FRACTION)
        )
    }

    @Test
    fun `mixed number fraction target`() {
        // 1.4375 in = 1 + 7/16 in = 92/64 in
        assertEquals(
            "1 7/16 in",
            UnitConverter.convert(BigDecimal("1.4375"), MeasurementUnit.IN, MeasurementUnit.FRACTION)
        )
    }

    @Test
    fun `value rounds to nearest 64th when not exact`() {
        // 0.2 in * 64 = 12.8 -> rounds to 13/64
        assertEquals(
            "13/64 in",
            UnitConverter.convert(BigDecimal("0.2"), MeasurementUnit.IN, MeasurementUnit.FRACTION)
        )
    }

    @Test
    fun `fraction denominator defaults to 64th when not specified`() {
        assertEquals(
            UnitConverter.convert(BigDecimal("0.2"), MeasurementUnit.IN, MeasurementUnit.FRACTION),
            UnitConverter.convert(BigDecimal("0.2"), MeasurementUnit.IN, MeasurementUnit.FRACTION, 64L)
        )
    }

    @Test
    fun `custom 16th precision rounds coarser than the 64th default`() {
        // 0.2 in * 16 = 3.2 -> rounds to 3/16 (not 13/64, the 64th-precision result)
        assertEquals(
            "3/16 in",
            UnitConverter.convert(BigDecimal("0.2"), MeasurementUnit.IN, MeasurementUnit.FRACTION, 16L)
        )
    }

    @Test
    fun `custom 32nd precision reduces to lowest terms`() {
        // 0.2 in * 32 = 6.4 -> rounds to 6/32, reduced to 3/16
        assertEquals(
            "3/16 in",
            UnitConverter.convert(BigDecimal("0.2"), MeasurementUnit.IN, MeasurementUnit.FRACTION, 32L)
        )
    }

    @Test
    fun `custom precision also applies to feet-inches conversions`() {
        assertEquals(
            "3/16 in",
            UnitConverter.convertFromFeetInches(BigDecimal.ZERO, BigDecimal("0.2"), MeasurementUnit.FRACTION, 16L)
        )
    }

    @Test
    fun `zero converts to zero regardless of target unit`() {
        assertEquals(
            "0 mm",
            UnitConverter.convert(BigDecimal.ZERO, MeasurementUnit.IN, MeasurementUnit.MM)
        )
    }

    @Test
    fun `mm to in is a rounded repeating decimal`() {
        // 100 / 25.4 = 3.937007874...
        assertEquals(
            "3.937008 in",
            UnitConverter.convert(BigDecimal("100"), MeasurementUnit.MM, MeasurementUnit.IN)
        )
    }

    @Test
    fun `negative feet-inches formats with a single leading sign`() {
        assertEquals(
            "-1 ft 0 in",
            UnitConverter.convert(BigDecimal("-12"), MeasurementUnit.IN, MeasurementUnit.FT_IN)
        )
    }

    // --- Length additions (mi, yd, km) ---

    @Test
    fun `1 mile is exactly 1760 yards`() {
        assertEquals("1760 yd", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.MI, MeasurementUnit.YD))
    }

    @Test
    fun `1 yard is exactly 36 inches`() {
        assertEquals("36 in", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.YD, MeasurementUnit.IN))
    }

    @Test
    fun `1 km to miles is a rounded repeating decimal`() {
        assertEquals("0.621371 mi", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.KM, MeasurementUnit.MI))
    }

    // --- Weight ---

    @Test
    fun `1 pound is exactly 453point59237 grams`() {
        assertEquals("453.59237 g", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.LB, MeasurementUnit.G))
    }

    @Test
    fun `1 kg to pounds is a rounded repeating decimal`() {
        assertEquals("2.204623 lb", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.KG, MeasurementUnit.LB))
    }

    @Test
    fun `16 ounces is exactly 1 pound`() {
        assertEquals("1 lb", UnitConverter.convert(BigDecimal("16"), MeasurementUnit.OZ, MeasurementUnit.LB))
    }

    @Test
    fun `1 stone is exactly 14 pounds`() {
        assertEquals("14 lb", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.STONE, MeasurementUnit.LB))
    }

    @Test
    fun `2000 pounds is exactly 1 US ton`() {
        assertEquals("1 ton", UnitConverter.convert(BigDecimal("2000"), MeasurementUnit.LB, MeasurementUnit.US_TON))
    }

    @Test
    fun `1 metric ton is exactly 1000 kg`() {
        assertEquals("1000 kg", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.METRIC_TON, MeasurementUnit.KG))
    }

    // --- Volume (US customary) ---

    @Test
    fun `1 gallon is exactly 4 quarts`() {
        assertEquals("4 qt", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.GALLON, MeasurementUnit.QUART))
    }

    @Test
    fun `1 gallon to liters is a rounded repeating decimal`() {
        assertEquals("3.785412 L", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.GALLON, MeasurementUnit.LITER))
    }

    @Test
    fun `1 cup is exactly 8 fluid ounces`() {
        assertEquals("8 fl oz", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.CUP, MeasurementUnit.FL_OZ))
    }

    @Test
    fun `3 teaspoons is exactly 1 tablespoon`() {
        assertEquals("1 tbsp", UnitConverter.convert(BigDecimal("3"), MeasurementUnit.TSP, MeasurementUnit.TBSP))
    }

    @Test
    fun `1 cubic meter is exactly 1000 liters`() {
        assertEquals("1000 L", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.CUBIC_METER, MeasurementUnit.LITER))
    }

    // --- Area ---

    @Test
    fun `1 acre is exactly 43560 square feet`() {
        assertEquals("43560 ft²", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.ACRE, MeasurementUnit.SQ_FT))
    }

    @Test
    fun `1 square mile is exactly 640 acres`() {
        assertEquals("640 acre", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.SQ_MI, MeasurementUnit.ACRE))
    }

    @Test
    fun `1 square meter to square feet is a rounded repeating decimal`() {
        assertEquals("10.76391 ft²", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.SQ_M, MeasurementUnit.SQ_FT))
    }

    @Test
    fun `1 hectare to acres is a rounded repeating decimal`() {
        assertEquals("2.471054 acre", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.HECTARE, MeasurementUnit.ACRE))
    }

    // --- Speed ---

    @Test
    fun `60 mph to kmh`() {
        assertEquals("96.56064 km/h", UnitConverter.convert(BigDecimal("60"), MeasurementUnit.MPH, MeasurementUnit.KMH))
    }

    @Test
    fun `100 kmh to mph`() {
        assertEquals("62.137119 mph", UnitConverter.convert(BigDecimal("100"), MeasurementUnit.KMH, MeasurementUnit.MPH))
    }

    @Test
    fun `1 knot to mph`() {
        assertEquals("1.150779 mph", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.KNOT, MeasurementUnit.MPH))
    }

    // --- Time ---

    @Test
    fun `90 minutes is exactly 1point5 hours`() {
        assertEquals("1.5 hr", UnitConverter.convert(BigDecimal("90"), MeasurementUnit.MINUTE, MeasurementUnit.HOUR))
    }

    @Test
    fun `1 day is exactly 24 hours`() {
        assertEquals("24 hr", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.DAY, MeasurementUnit.HOUR))
    }

    @Test
    fun `1 hour is exactly 3600 seconds`() {
        assertEquals("3600 s", UnitConverter.convert(BigDecimal("1"), MeasurementUnit.HOUR, MeasurementUnit.SECOND))
    }

    // --- Temperature ---

    @Test
    fun `0 celsius is exactly 32 fahrenheit`() {
        assertEquals("32 °F", UnitConverter.convert(BigDecimal("0"), MeasurementUnit.CELSIUS, MeasurementUnit.FAHRENHEIT))
    }

    @Test
    fun `100 celsius (boiling point) is exactly 212 fahrenheit`() {
        assertEquals("212 °F", UnitConverter.convert(BigDecimal("100"), MeasurementUnit.CELSIUS, MeasurementUnit.FAHRENHEIT))
    }

    @Test
    fun `32 fahrenheit is exactly 0 celsius`() {
        assertEquals("0 °C", UnitConverter.convert(BigDecimal("32"), MeasurementUnit.FAHRENHEIT, MeasurementUnit.CELSIUS))
    }

    @Test
    fun `98point6 fahrenheit (body temp) is exactly 37 celsius`() {
        assertEquals("37 °C", UnitConverter.convert(BigDecimal("98.6"), MeasurementUnit.FAHRENHEIT, MeasurementUnit.CELSIUS))
    }

    @Test
    fun `0 celsius is exactly 273point15 kelvin`() {
        assertEquals("273.15 K", UnitConverter.convert(BigDecimal("0"), MeasurementUnit.CELSIUS, MeasurementUnit.KELVIN))
    }

    @Test
    fun `273point15 kelvin is exactly 0 celsius`() {
        assertEquals("0 °C", UnitConverter.convert(BigDecimal("273.15"), MeasurementUnit.KELVIN, MeasurementUnit.CELSIUS))
    }

    @Test
    fun `minus 40 celsius equals minus 40 fahrenheit (the crossing point)`() {
        assertEquals("-40 °F", UnitConverter.convert(BigDecimal("-40"), MeasurementUnit.CELSIUS, MeasurementUnit.FAHRENHEIT))
        assertEquals("-40 °C", UnitConverter.convert(BigDecimal("-40"), MeasurementUnit.FAHRENHEIT, MeasurementUnit.CELSIUS))
    }

    @Test
    fun `converting a unit to itself within a category is a no-op`() {
        assertEquals("5 kg", UnitConverter.convert(BigDecimal("5"), MeasurementUnit.KG, MeasurementUnit.KG))
        assertEquals("98.6 °F", UnitConverter.convert(BigDecimal("98.6"), MeasurementUnit.FAHRENHEIT, MeasurementUnit.FAHRENHEIT))
    }

    @Test
    fun `every category has a default source-target pair within that same category`() {
        UnitCategory.entries.forEach { category ->
            val (source, target) = MeasurementUnit.defaultsFor(category)
            assertEquals(category, source.category)
            assertEquals(category, target.category)
        }
    }
}
