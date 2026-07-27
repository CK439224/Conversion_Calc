package com.numconversion.app.domain.fraction

/**
 * Formats a numerator/denominator pair as a reduced whole-number-plus-fraction string,
 * e.g. 10/2 -> "5", 7/5 -> "1 2/5", 3/4 -> "3/4", -7/5 -> "-1 2/5".
 */
object FractionFormatter {

    fun format(numerator: Long, denominator: Long): String {
        require(denominator != 0L) { "Denominator cannot be zero" }
        if (numerator == 0L) return "0"

        val reduced = Fraction(numerator, denominator).reduced()
        val whole = reduced.numerator / reduced.denominator
        val remainder = kotlin.math.abs(reduced.numerator % reduced.denominator)

        return when {
            remainder == 0L -> whole.toString()
            whole == 0L -> "${reduced.numerator}/${reduced.denominator}"
            else -> "$whole $remainder/${reduced.denominator}"
        }
    }
}
