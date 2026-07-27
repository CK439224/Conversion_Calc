package com.numconversion.app.domain.fraction

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class FractionFormatterTest {

    @Test
    fun `evenly divisible fraction reduces to whole number`() {
        assertEquals("5", FractionFormatter.format(10, 2))
    }

    @Test
    fun `improper fraction splits into whole and remainder`() {
        assertEquals("1 2/5", FractionFormatter.format(7, 5))
    }

    @Test
    fun `already-simplified proper fraction is unchanged`() {
        assertEquals("3/4", FractionFormatter.format(3, 4))
    }

    @Test
    fun `unreduced proper fraction is simplified`() {
        assertEquals("1/2", FractionFormatter.format(2, 4))
    }

    @Test
    fun `zero numerator is zero`() {
        assertEquals("0", FractionFormatter.format(0, 5))
    }

    @Test
    fun `negative improper fraction keeps single leading sign`() {
        assertEquals("-1 2/5", FractionFormatter.format(-7, 5))
    }

    @Test
    fun `negative proper fraction keeps single leading sign`() {
        assertEquals("-3/5", FractionFormatter.format(-3, 5))
    }

    @Test
    fun `negative denominator normalizes sign onto numerator`() {
        assertEquals("-3/5", FractionFormatter.format(3, -5))
    }

    @Test
    fun `double negative cancels to positive`() {
        assertEquals("3/5", FractionFormatter.format(-3, -5))
    }

    @Test
    fun `zero denominator throws`() {
        assertThrows(IllegalArgumentException::class.java) {
            FractionFormatter.format(1, 0)
        }
    }
}
