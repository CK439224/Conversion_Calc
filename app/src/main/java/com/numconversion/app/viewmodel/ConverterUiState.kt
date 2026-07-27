package com.numconversion.app.viewmodel

import com.numconversion.app.domain.conversion.MeasurementUnit

/** Which input box the shared keypad currently writes digits into. */
enum class ConverterField { SINGLE, FEET, INCHES }

/** State for the independent Convert screen. */
data class ConverterUiState(
    val sourceUnit: MeasurementUnit = MeasurementUnit.MM,
    val targetUnit: MeasurementUnit = MeasurementUnit.IN,
    val input: String = "",
    val feetInput: String = "",
    val inchesInput: String = "",
    val activeField: ConverterField = ConverterField.SINGLE,
    val result: String = "",
    val errorMessage: String? = null
) {
    val isFeetInchesSource: Boolean get() = sourceUnit == MeasurementUnit.FT_IN
}
