package com.numconversion.app.viewmodel

import com.numconversion.app.domain.conversion.FractionPrecision
import com.numconversion.app.domain.conversion.MeasurementUnit
import com.numconversion.app.ui.theme.ColorPalette
import com.numconversion.app.ui.theme.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel()
    }

    // --- Calculator ---

    @Test
    fun `digits build up the display`() {
        viewModel.onCalculatorDigit('1')
        viewModel.onCalculatorDigit('2')
        assertEquals("12", viewModel.calculatorState.value.display)
    }

    @Test
    fun `leading zero is replaced by the next digit`() {
        viewModel.onCalculatorDigit('0')
        viewModel.onCalculatorDigit('5')
        assertEquals("5", viewModel.calculatorState.value.display)
    }

    @Test
    fun `full expression evaluates on equals`() {
        "12+3*4".forEach { c ->
            if (c.isDigit()) viewModel.onCalculatorDigit(c) else viewModel.onCalculatorOperator(c)
        }
        viewModel.onCalculatorEquals()
        assertEquals("24", viewModel.calculatorState.value.display)
        assertFalse(viewModel.calculatorState.value.hasError)
    }

    @Test
    fun `divide by zero surfaces an error state`() {
        viewModel.onCalculatorDigit('5')
        viewModel.onCalculatorOperator('/')
        viewModel.onCalculatorDigit('0')
        viewModel.onCalculatorEquals()
        assertTrue(viewModel.calculatorState.value.hasError)
    }

    @Test
    fun `digit after an error starts a fresh expression`() {
        viewModel.onCalculatorDigit('5')
        viewModel.onCalculatorOperator('/')
        viewModel.onCalculatorDigit('0')
        viewModel.onCalculatorEquals()
        viewModel.onCalculatorDigit('7')
        val state = viewModel.calculatorState.value
        assertFalse(state.hasError)
        assertEquals("7", state.display)
    }

    @Test
    fun `clear resets to zero`() {
        viewModel.onCalculatorDigit('9')
        viewModel.onCalculatorClear()
        assertEquals("0", viewModel.calculatorState.value.display)
    }

    @Test
    fun `backspace removes the last character`() {
        viewModel.onCalculatorDigit('1')
        viewModel.onCalculatorDigit('2')
        viewModel.onCalculatorBackspace()
        assertEquals("1", viewModel.calculatorState.value.display)
    }

    @Test
    fun `parenthesis key inserts open then close`() {
        viewModel.onCalculatorParenthesis()
        viewModel.onCalculatorDigit('5')
        viewModel.onCalculatorParenthesis()
        assertEquals("(5)", viewModel.calculatorState.value.display)
    }

    @Test
    fun `fraction key evaluates numerator over denominator`() {
        viewModel.onCalculatorDigit('7')
        viewModel.onCalculatorFractionKey()
        viewModel.onCalculatorDigit('5')
        assertEquals("7/5", viewModel.calculatorState.value.display)
        viewModel.onCalculatorEquals()
        assertEquals("1 2/5", viewModel.calculatorState.value.display)
    }

    @Test
    fun `operators are ignored while entering a fraction`() {
        viewModel.onCalculatorDigit('3')
        viewModel.onCalculatorFractionKey()
        viewModel.onCalculatorOperator('+')
        viewModel.onCalculatorDigit('4')
        assertEquals("3/4", viewModel.calculatorState.value.display)
    }

    @Test
    fun `backspace on empty denominator backs out of fraction entry`() {
        viewModel.onCalculatorDigit('3')
        viewModel.onCalculatorFractionKey()
        viewModel.onCalculatorBackspace()
        assertFalse(viewModel.calculatorState.value.isEnteringFraction)
        assertEquals("3", viewModel.calculatorState.value.display)
    }

    // --- Converter ---

    @Test
    fun `converter live-updates as digits are typed`() {
        viewModel.onSourceUnitChange(MeasurementUnit.MM)
        viewModel.onTargetUnitChange(MeasurementUnit.IN)
        viewModel.onConverterDigit('2')
        viewModel.onConverterDigit('5')
        viewModel.onConverterDecimal()
        viewModel.onConverterDigit('4')
        assertEquals("1 in", viewModel.converterState.value.result)
    }

    @Test
    fun `switching source unit to FT_IN routes digits to the feet field`() {
        viewModel.onSourceUnitChange(MeasurementUnit.FT_IN)
        viewModel.onTargetUnitChange(MeasurementUnit.IN)
        viewModel.onConverterDigit('5')
        viewModel.onConverterFieldFocused(ConverterField.INCHES)
        viewModel.onConverterDigit('4')
        val state = viewModel.converterState.value
        assertEquals("5", state.feetInput)
        assertEquals("4", state.inchesInput)
        assertEquals("64 in", state.result)
    }

    @Test
    fun `changing source unit resets prior input`() {
        viewModel.onConverterDigit('5')
        viewModel.onSourceUnitChange(MeasurementUnit.FT_IN)
        assertEquals("", viewModel.converterState.value.input)
        assertEquals("", viewModel.converterState.value.feetInput)
    }

    @Test
    fun `empty input produces no result`() {
        assertEquals("", viewModel.converterState.value.result)
    }

    @Test
    fun `calculator and converter state are independent`() {
        viewModel.onCalculatorDigit('9')
        viewModel.onConverterDigit('1')
        assertEquals("9", viewModel.calculatorState.value.display)
        assertEquals("1", viewModel.converterState.value.input)
    }

    // --- History ---

    @Test
    fun `successful calculator equals logs a history entry`() {
        viewModel.onCalculatorDigit('1')
        viewModel.onCalculatorOperator('+')
        viewModel.onCalculatorDigit('2')
        viewModel.onCalculatorEquals()
        val history = viewModel.historyState.value
        assertEquals(1, history.size)
        assertEquals(com.numconversion.app.domain.history.HistoryEntryType.CALCULATOR, history[0].type)
        assertEquals("1+2 = 3", history[0].summary)
    }

    @Test
    fun `calculator error does not log a history entry`() {
        viewModel.onCalculatorDigit('5')
        viewModel.onCalculatorOperator('/')
        viewModel.onCalculatorDigit('0')
        viewModel.onCalculatorEquals()
        assertTrue(viewModel.historyState.value.isEmpty())
    }

    @Test
    fun `fraction equals logs a history entry`() {
        viewModel.onCalculatorDigit('7')
        viewModel.onCalculatorFractionKey()
        viewModel.onCalculatorDigit('5')
        viewModel.onCalculatorEquals()
        val history = viewModel.historyState.value
        assertEquals(1, history.size)
        assertEquals("7/5 = 1 2/5", history[0].summary)
    }

    @Test
    fun `converter does not log until the result is copied`() {
        viewModel.onConverterDigit('2')
        viewModel.onConverterDigit('5')
        viewModel.onConverterDecimal()
        viewModel.onConverterDigit('4')
        assertTrue(viewModel.historyState.value.isEmpty())

        viewModel.onConverterResultCopied()
        val history = viewModel.historyState.value
        assertEquals(1, history.size)
        assertEquals(com.numconversion.app.domain.history.HistoryEntryType.CONVERTER, history[0].type)
        assertEquals("25.4 mm → 1 in", history[0].summary)
    }

    @Test
    fun `copying a blank converter result does not log`() {
        viewModel.onConverterResultCopied()
        assertTrue(viewModel.historyState.value.isEmpty())
    }

    @Test
    fun `feet-inches converter summary describes both fields`() {
        viewModel.onSourceUnitChange(MeasurementUnit.FT_IN)
        viewModel.onTargetUnitChange(MeasurementUnit.IN)
        viewModel.onConverterDigit('5')
        viewModel.onConverterFieldFocused(ConverterField.INCHES)
        viewModel.onConverterDigit('4')
        viewModel.onConverterResultCopied()
        assertEquals("5 ft 4 in → 64 in", viewModel.historyState.value[0].summary)
    }

    @Test
    fun `history is newest first and capped at 100 entries`() {
        repeat(105) { i ->
            viewModel.onCalculatorClear()
            i.toString().forEach { viewModel.onCalculatorDigit(it) }
            viewModel.onCalculatorOperator('+')
            viewModel.onCalculatorDigit('1')
            viewModel.onCalculatorEquals()
        }
        val history = viewModel.historyState.value
        assertEquals(100, history.size)
        assertEquals("104+1 = 105", history.first().summary)
    }

    @Test
    fun `clear history empties the list`() {
        viewModel.onCalculatorDigit('1')
        viewModel.onCalculatorOperator('+')
        viewModel.onCalculatorDigit('1')
        viewModel.onCalculatorEquals()
        assertEquals(1, viewModel.historyState.value.size)

        viewModel.onClearHistory()
        assertTrue(viewModel.historyState.value.isEmpty())
    }

    // --- Settings ---

    @Test
    fun `settings start on defaults with no pending change`() {
        val state = viewModel.settingsState.value
        assertEquals(AppSettings(), state.effective)
        assertFalse(state.hasPendingChange)
    }

    @Test
    fun `previewing a setting updates effective without touching applied`() {
        viewModel.onThemeModePreviewed(ThemeMode.DARK)
        val state = viewModel.settingsState.value
        assertEquals(ThemeMode.DARK, state.effective.themeMode)
        assertEquals(ThemeMode.Default, state.applied.themeMode)
        assertTrue(state.hasPendingChange)
    }

    @Test
    fun `applying commits the preview and clears it`() {
        viewModel.onPalettePreviewed(ColorPalette.OCEAN_BLUE)
        viewModel.onSettingsApplied()
        val state = viewModel.settingsState.value
        assertEquals(ColorPalette.OCEAN_BLUE, state.applied.palette)
        assertNull(state.preview)
        assertFalse(state.hasPendingChange)
    }

    @Test
    fun `discarding a preview reverts to the applied settings`() {
        viewModel.onHapticsPreviewed(false)
        viewModel.onSettingsPreviewDiscarded()
        val state = viewModel.settingsState.value
        assertTrue(state.effective.hapticsEnabled)
        assertNull(state.preview)
    }

    @Test
    fun `applying with nothing previewed is a no-op`() {
        viewModel.onSettingsApplied()
        assertEquals(AppSettings(), viewModel.settingsState.value.applied)
    }

    @Test
    fun `multiple previews in one transaction are all discarded together on cancel`() {
        viewModel.onThemeModePreviewed(ThemeMode.DARK)
        viewModel.onFractionPrecisionPreviewed(FractionPrecision.SIXTEENTH)
        viewModel.onSettingsPreviewDiscarded()
        val state = viewModel.settingsState.value
        assertEquals(ThemeMode.Default, state.effective.themeMode)
        assertEquals(FractionPrecision.Default, state.effective.fractionPrecision)
    }

    @Test
    fun `multiple previews in one transaction are all committed together on apply`() {
        viewModel.onThemeModePreviewed(ThemeMode.DARK)
        viewModel.onFractionPrecisionPreviewed(FractionPrecision.SIXTEENTH)
        viewModel.onSettingsApplied()
        val state = viewModel.settingsState.value
        assertEquals(ThemeMode.DARK, state.applied.themeMode)
        assertEquals(FractionPrecision.SIXTEENTH, state.applied.fractionPrecision)
    }

    @Test
    fun `previewing fraction precision instantly changes an already-displayed converter result`() {
        viewModel.onSourceUnitChange(MeasurementUnit.IN)
        viewModel.onTargetUnitChange(MeasurementUnit.FRACTION)
        viewModel.onConverterDigit('0')
        viewModel.onConverterDecimal()
        viewModel.onConverterDigit('2')
        assertEquals("13/64 in", viewModel.converterState.value.result)

        viewModel.onFractionPrecisionPreviewed(FractionPrecision.SIXTEENTH)
        assertEquals("3/16 in", viewModel.converterState.value.result)
    }

    @Test
    fun `discarding a fraction precision preview reverts the converter result`() {
        viewModel.onSourceUnitChange(MeasurementUnit.IN)
        viewModel.onTargetUnitChange(MeasurementUnit.FRACTION)
        viewModel.onConverterDigit('0')
        viewModel.onConverterDecimal()
        viewModel.onConverterDigit('2')

        viewModel.onFractionPrecisionPreviewed(FractionPrecision.SIXTEENTH)
        viewModel.onSettingsPreviewDiscarded()
        assertEquals("13/64 in", viewModel.converterState.value.result)
    }

    @Test
    fun `settings preview and apply do not crash without a settings repository`() {
        viewModel.onPalettePreviewed(ColorPalette.CRIMSON_ROSE)
        viewModel.onThemeModePreviewed(ThemeMode.LIGHT)
        viewModel.onHapticsPreviewed(false)
        viewModel.onFractionPrecisionPreviewed(FractionPrecision.THIRTY_SECOND)
        viewModel.onSettingsApplied()
        val state = viewModel.settingsState.value
        assertEquals(ColorPalette.CRIMSON_ROSE, state.applied.palette)
        assertEquals(ThemeMode.LIGHT, state.applied.themeMode)
        assertFalse(state.applied.hapticsEnabled)
        assertEquals(FractionPrecision.THIRTY_SECOND, state.applied.fractionPrecision)
    }

    @Test
    fun `changing converter units does not crash without a settings repository`() {
        viewModel.onSourceUnitChange(MeasurementUnit.FT_IN)
        viewModel.onTargetUnitChange(MeasurementUnit.MM)
        assertEquals(MeasurementUnit.FT_IN, viewModel.converterState.value.sourceUnit)
        assertEquals(MeasurementUnit.MM, viewModel.converterState.value.targetUnit)
    }
}
