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
}
