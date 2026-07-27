package com.numconversion.app.viewmodel

/** State for the independent Calculator screen. */
data class CalculatorUiState(
    /** The expression being typed, or (while entering a fraction) the denominator being typed. */
    val expression: String = "",
    /** What the big answer display shows: the live expression, an evaluated result, or an error. */
    val display: String = "0",
    val hasError: Boolean = false,
    val isEnteringFraction: Boolean = false,
    val fractionNumerator: String = ""
)
