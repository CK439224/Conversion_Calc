package com.numconversion.app.viewmodel

import com.numconversion.app.domain.conversion.MeasurementUnit
import com.numconversion.app.domain.conversion.UnitCategory

/** Which input box the shared keypad currently writes digits into. */
enum class ConverterField { SINGLE, FEET, INCHES }

/** State for the independent Convert screen. There's no separate "selected category" field —
 *  the category is always [sourceUnit]'s, and switching category resets both units at once via
 *  [MainViewModel.onCategoryChange] rather than tracking category as independent state. */
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
    val category: UnitCategory get() = sourceUnit.category
}
