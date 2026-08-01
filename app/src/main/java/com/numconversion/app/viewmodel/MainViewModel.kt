package com.numconversion.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numconversion.app.data.settings.SettingsRepository
import com.numconversion.app.domain.conversion.FractionPrecision
import com.numconversion.app.domain.conversion.MeasurementUnit
import com.numconversion.app.domain.conversion.UnitCategory
import com.numconversion.app.domain.conversion.UnitConverter
import com.numconversion.app.domain.engine.CalculatorResult
import com.numconversion.app.domain.engine.Evaluator
import com.numconversion.app.domain.fraction.FractionFormatter
import com.numconversion.app.domain.history.HistoryEntry
import com.numconversion.app.domain.history.HistoryEntryType
import com.numconversion.app.ui.theme.ColorPalette
import com.numconversion.app.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

/**
 * Holds four independent UI-state slices — calculator, converter, history, and settings — so the
 * calculator and converter screens can be used without affecting each other, per the app's
 * requirement that they work independently. [settingsRepository] is nullable/optional (rather than
 * requiring an Android [android.content.Context] in the constructor, e.g. via AndroidViewModel) so
 * plain JUnit tests can keep constructing this with `MainViewModel()` — there is no Robolectric in
 * this project, so any real Context use would crash a local unit test.
 */
class MainViewModel(private val settingsRepository: SettingsRepository? = null) : ViewModel() {

    private val _calculatorState = MutableStateFlow(withDisplay(CalculatorUiState()))
    val calculatorState: StateFlow<CalculatorUiState> = _calculatorState.asStateFlow()

    private val _converterState = MutableStateFlow(ConverterUiState())
    val converterState: StateFlow<ConverterUiState> = _converterState.asStateFlow()

    private val _historyState = MutableStateFlow<List<HistoryEntry>>(emptyList())
    val historyState: StateFlow<List<HistoryEntry>> = _historyState.asStateFlow()

    private val _settingsState = MutableStateFlow(SettingsUiState())
    val settingsState: StateFlow<SettingsUiState> = _settingsState.asStateFlow()

    private var nextHistoryId = 0L

    init {
        settingsRepository?.let { repository ->
            viewModelScope.launch {
                repository.appSettings.collect { settings ->
                    _settingsState.update { it.copy(applied = settings) }
                    refreshConverterForSettingsChange()
                }
            }
            viewModelScope.launch {
                val (source, target) = repository.lastUnits.first()
                restoreLastUnits(source, target)
            }
        }
    }

    // ---------------------------------------------------------------------
    // Settings
    // ---------------------------------------------------------------------

    /** Called as the user touches each control in the settings dialog — applies instantly. */
    fun onPalettePreviewed(palette: ColorPalette) = updateSettingsPreview { it.copy(palette = palette) }

    fun onThemeModePreviewed(themeMode: ThemeMode) = updateSettingsPreview { it.copy(themeMode = themeMode) }

    fun onHapticsPreviewed(enabled: Boolean) = updateSettingsPreview { it.copy(hapticsEnabled = enabled) }

    fun onFractionPrecisionPreviewed(precision: FractionPrecision) =
        updateSettingsPreview { it.copy(fractionPrecision = precision) }

    private fun updateSettingsPreview(transform: (AppSettings) -> AppSettings) {
        _settingsState.update { it.copy(preview = transform(it.preview ?: it.applied)) }
        refreshConverterForSettingsChange()
    }

    /** Commits every previewed setting and persists it; no-op if nothing was previewed. */
    fun onSettingsApplied() {
        val target = _settingsState.value.preview ?: return
        _settingsState.update { it.copy(applied = target, preview = null) }
        settingsRepository?.let { repository ->
            viewModelScope.launch { repository.saveAppSettings(target) }
        }
        refreshConverterForSettingsChange()
    }

    /** Discards every in-progress preview (dialog cancelled/dismissed) without persisting it. */
    fun onSettingsPreviewDiscarded() {
        _settingsState.update { it.copy(preview = null) }
        refreshConverterForSettingsChange()
    }

    /** The converter's FRACTION target unit depends on the live-previewed fraction precision. */
    private fun refreshConverterForSettingsChange() {
        _converterState.update { recomputeConversion(it) }
    }

    // ---------------------------------------------------------------------
    // Calculator
    // ---------------------------------------------------------------------

    fun onCalculatorDigit(digit: Char) {
        _calculatorState.update { state ->
            val cleared = if (state.hasError) CalculatorUiState() else state
            withDisplay(mutateActiveBuffer(cleared) { current -> if (current == "0") digit.toString() else current + digit })
        }
    }

    fun onCalculatorDecimal() {
        _calculatorState.update { state ->
            val cleared = if (state.hasError) CalculatorUiState() else state
            withDisplay(mutateActiveBuffer(cleared) { current -> if (current.contains('.')) current else "$current." })
        }
    }

    fun onCalculatorOperator(operator: Char) {
        _calculatorState.update { state ->
            if (state.isEnteringFraction || state.hasError) return@update state
            withDisplay(state.copy(expression = state.expression + operator))
        }
    }

    fun onCalculatorParenthesis() {
        _calculatorState.update { state ->
            if (state.isEnteringFraction || state.hasError) return@update state
            val balance = state.expression.count { it == '(' } - state.expression.count { it == ')' }
            val lastChar = state.expression.lastOrNull()
            val insertClose = balance > 0 && (lastChar?.isDigit() == true || lastChar == ')')
            withDisplay(state.copy(expression = state.expression + if (insertClose) ')' else '('))
        }
    }

    fun onCalculatorFractionKey() {
        _calculatorState.update { state ->
            if (state.hasError) return@update withDisplay(CalculatorUiState())
            if (state.isEnteringFraction) return@update state
            withDisplay(state.copy(isEnteringFraction = true, fractionNumerator = state.expression, expression = ""))
        }
    }

    fun onCalculatorBackspace() {
        _calculatorState.update { state ->
            when {
                state.hasError -> withDisplay(CalculatorUiState())
                state.isEnteringFraction && state.expression.isEmpty() ->
                    withDisplay(state.copy(isEnteringFraction = false, expression = state.fractionNumerator, fractionNumerator = ""))
                else -> withDisplay(mutateActiveBuffer(state) { it.dropLast(1) })
            }
        }
    }

    fun onCalculatorClear() {
        _calculatorState.update { withDisplay(CalculatorUiState()) }
    }

    fun onCalculatorEquals() {
        val state = _calculatorState.value
        if (state.isEnteringFraction) {
            val numerator = state.fractionNumerator.toLongOrNull()
            val denominator = state.expression.toLongOrNull()
            if (numerator == null || denominator == null || denominator == 0L) {
                _calculatorState.update { withDisplay(CalculatorUiState(display = "Invalid fraction", hasError = true)) }
            } else {
                val formatted = FractionFormatter.format(numerator, denominator)
                addHistoryEntry(
                    HistoryEntryType.CALCULATOR,
                    summary = "${state.fractionNumerator}/${state.expression} = $formatted",
                    resultText = formatted
                )
                _calculatorState.update { withDisplay(CalculatorUiState(expression = formatted, display = formatted)) }
            }
        } else {
            when (val result = Evaluator.evaluate(state.expression)) {
                is CalculatorResult.Value -> {
                    val text = result.value.toPlainString()
                    addHistoryEntry(
                        HistoryEntryType.CALCULATOR,
                        summary = "${state.expression} = $text",
                        resultText = text
                    )
                    _calculatorState.update { withDisplay(CalculatorUiState(expression = text, display = text)) }
                }
                is CalculatorResult.Error ->
                    _calculatorState.update { withDisplay(CalculatorUiState(display = result.message, hasError = true)) }
            }
        }
    }

    private fun mutateActiveBuffer(state: CalculatorUiState, transform: (String) -> String): CalculatorUiState =
        state.copy(expression = transform(state.expression))

    private fun withDisplay(state: CalculatorUiState): CalculatorUiState {
        if (state.hasError) return state
        val display = if (state.isEnteringFraction) {
            "${state.fractionNumerator}/${state.expression}"
        } else {
            state.expression.ifEmpty { "0" }
        }
        return state.copy(display = display)
    }

    // ---------------------------------------------------------------------
    // Converter
    // ---------------------------------------------------------------------

    fun onSourceUnitChange(unit: MeasurementUnit) {
        _converterState.update {
            val activeField = if (unit == MeasurementUnit.FT_IN) ConverterField.FEET else ConverterField.SINGLE
            recomputeConversion(
                it.copy(sourceUnit = unit, activeField = activeField, input = "", feetInput = "", inchesInput = "")
            )
        }
        persistLastUnits()
    }

    fun onTargetUnitChange(unit: MeasurementUnit) {
        _converterState.update { recomputeConversion(it.copy(targetUnit = unit)) }
        persistLastUnits()
    }

    /** Switching category resets both source and target to that category's default pair at once
     *  (unlike [onSourceUnitChange], which only ever changes one side) — a unit from the old
     *  category wouldn't be valid to keep as the other side of the new one. */
    fun onCategoryChange(category: UnitCategory) {
        val (defaultSource, defaultTarget) = MeasurementUnit.defaultsFor(category)
        _converterState.update {
            val activeField = if (defaultSource == MeasurementUnit.FT_IN) ConverterField.FEET else ConverterField.SINGLE
            recomputeConversion(
                ConverterUiState(
                    sourceUnit = defaultSource,
                    targetUnit = defaultTarget,
                    activeField = activeField
                )
            )
        }
        persistLastUnits()
    }

    /** One-shot restore of the persisted unit pair at startup — skips the reset/wipe that
     *  [onSourceUnitChange] does, since there's no user input yet to preserve or clear, and must
     *  not re-persist what it just loaded. */
    private fun restoreLastUnits(sourceUnit: MeasurementUnit, targetUnit: MeasurementUnit) {
        _converterState.update {
            val activeField = if (sourceUnit == MeasurementUnit.FT_IN) ConverterField.FEET else ConverterField.SINGLE
            recomputeConversion(it.copy(sourceUnit = sourceUnit, targetUnit = targetUnit, activeField = activeField))
        }
    }

    private fun persistLastUnits() {
        val repository = settingsRepository ?: return
        val state = _converterState.value
        viewModelScope.launch { repository.saveLastUnits(state.sourceUnit, state.targetUnit) }
    }

    fun onConverterFieldFocused(field: ConverterField) {
        _converterState.update { it.copy(activeField = field) }
    }

    fun onConverterDigit(digit: Char) {
        _converterState.update {
            recomputeConversion(mutateActiveField(it) { current -> if (current == "0") digit.toString() else current + digit })
        }
    }

    fun onConverterDecimal() {
        _converterState.update {
            recomputeConversion(mutateActiveField(it) { current -> if (current.contains('.')) current else "$current." })
        }
    }

    fun onConverterBackspace() {
        _converterState.update { recomputeConversion(mutateActiveField(it) { current -> current.dropLast(1) }) }
    }

    fun onConverterClear() {
        _converterState.update { ConverterUiState(sourceUnit = it.sourceUnit, targetUnit = it.targetUnit, activeField = it.activeField) }
    }

    private fun mutateActiveField(state: ConverterUiState, transform: (String) -> String): ConverterUiState =
        when (state.activeField) {
            ConverterField.SINGLE -> state.copy(input = transform(state.input))
            ConverterField.FEET -> state.copy(feetInput = transform(state.feetInput))
            ConverterField.INCHES -> state.copy(inchesInput = transform(state.inchesInput))
        }

    private fun recomputeConversion(state: ConverterUiState): ConverterUiState {
        val fractionDenominator = _settingsState.value.effective.fractionPrecision.denominator
        return try {
            if (state.isFeetInchesSource) {
                val feet = state.feetInput.parseBigDecimalOrNull()
                val inches = state.inchesInput.parseBigDecimalOrNull()
                if (feet == null && inches == null) {
                    state.copy(result = "", errorMessage = null)
                } else {
                    val result = UnitConverter.convertFromFeetInches(
                        feet ?: BigDecimal.ZERO,
                        inches ?: BigDecimal.ZERO,
                        state.targetUnit,
                        fractionDenominator
                    )
                    state.copy(result = result, errorMessage = null)
                }
            } else {
                val value = state.input.parseBigDecimalOrNull()
                if (value == null) {
                    state.copy(result = "", errorMessage = null)
                } else {
                    state.copy(
                        result = UnitConverter.convert(value, state.sourceUnit, state.targetUnit, fractionDenominator),
                        errorMessage = null
                    )
                }
            }
        } catch (e: Exception) {
            state.copy(result = "", errorMessage = e.message ?: "Invalid input")
        }
    }

    private fun String.parseBigDecimalOrNull(): BigDecimal? {
        if (isBlank() || this == "." || this == "-") return null
        return try {
            BigDecimal(this)
        } catch (e: NumberFormatException) {
            null
        }
    }

    /** Converter has no "=" — copying the result is its natural "I want this value" commit point. */
    fun onConverterResultCopied() {
        val state = _converterState.value
        if (state.result.isBlank() || state.errorMessage != null) return
        addHistoryEntry(
            HistoryEntryType.CONVERTER,
            summary = buildConverterSummary(state),
            resultText = state.result
        )
    }

    private fun buildConverterSummary(state: ConverterUiState): String {
        val sourceText = if (state.isFeetInchesSource) {
            val feet = state.feetInput.ifBlank { "0" }
            val inches = state.inchesInput.ifBlank { "0" }
            "$feet ft $inches in"
        } else {
            // FRACTION's own displayLabel is "Fraction" (a dropdown label, not a unit suffix) —
            // the value the user actually typed is a plain decimal number of inches.
            val unitAbbreviation = if (state.sourceUnit == MeasurementUnit.FRACTION) "in" else state.sourceUnit.displayLabel
            "${state.input} $unitAbbreviation"
        }
        return "$sourceText → ${state.result}"
    }

    // ---------------------------------------------------------------------
    // History
    // ---------------------------------------------------------------------

    fun onClearHistory() {
        _historyState.update { emptyList() }
    }

    private fun addHistoryEntry(type: HistoryEntryType, summary: String, resultText: String) {
        _historyState.update { current ->
            val entry = HistoryEntry(
                id = nextHistoryId++,
                type = type,
                summary = summary,
                resultText = resultText,
                timestampMillis = System.currentTimeMillis()
            )
            (listOf(entry) + current).take(MAX_HISTORY_ENTRIES)
        }
    }

    private companion object {
        const val MAX_HISTORY_ENTRIES = 100
    }
}
