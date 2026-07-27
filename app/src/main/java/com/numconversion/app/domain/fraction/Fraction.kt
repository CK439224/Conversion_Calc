package com.numconversion.app.domain.fraction

/** An exact numerator/denominator pair. Sign is normalized onto the numerator. */
data class Fraction(val numerator: Long, val denominator: Long) {

    init {
        require(denominator != 0L) { "Denominator cannot be zero" }
    }

    /** Returns this fraction in lowest terms with the denominator always positive. */
    fun reduced(): Fraction {
        val sign = if ((numerator < 0) != (denominator < 0)) -1L else 1L
        val n = kotlin.math.abs(numerator)
        val d = kotlin.math.abs(denominator)
        val g = gcd(n, d)
        return Fraction(sign * (n / g), d / g)
    }

    private fun gcd(a: Long, b: Long): Long {
        var x = a
        var y = b
        while (y != 0L) {
            val t = y
            y = x % y
            x = t
        }
        return if (x == 0L) 1L else x
    }
}
