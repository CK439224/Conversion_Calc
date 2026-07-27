package com.numconversion.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.numconversion.app.R
import com.numconversion.app.ui.theme.calculatorColors

/** Calculator mode enables every key; Converter mode only needs digits/decimal/clear/backspace. */
enum class KeypadMode { CALCULATOR, CONVERTER }

data class KeypadActions(
    val onDigit: (Char) -> Unit,
    val onDecimal: () -> Unit,
    val onClear: () -> Unit,
    val onBackspace: () -> Unit,
    val onOperator: (Char) -> Unit = {},
    val onParenthesis: () -> Unit = {},
    val onFraction: () -> Unit = {},
    val onEquals: () -> Unit = {}
)

/**
 * The shared 5x4 button grid used by both the Calculator and Convert screens.
 * Row1: C, backspace, a/b (fraction entry), ÷ — Row5: (), 0, ., =
 * Digit/function keys use CalcButton's default tint; operators and equals get their own
 * container/content colors here for the digit-tint < operator < equals fill hierarchy.
 */
@Composable
fun KeypadGrid(
    mode: KeypadMode,
    actions: KeypadActions,
    modifier: Modifier = Modifier
) {
    val isCalculator = mode == KeypadMode.CALCULATOR
    val colors = MaterialTheme.calculatorColors
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        KeypadRow(Modifier.height(72.dp)) {
            CalcButton("C", actions.onClear, Modifier.weight(1f), contentDescription = stringResource(R.string.cd_clear))
            CalcButton("⌫", actions.onBackspace, Modifier.weight(1f), contentDescription = stringResource(R.string.cd_backspace))
            CalcButton(
                "a/b",
                actions.onFraction,
                Modifier.weight(1f),
                enabled = isCalculator,
                contentDescription = stringResource(R.string.cd_fraction_entry)
            )
            CalcButton(
                "÷", { actions.onOperator('÷') }, Modifier.weight(1f), enabled = isCalculator,
                containerColor = colors.keyOperatorContainer, contentColor = colors.keyOperatorContent
            )
        }
        KeypadRow(Modifier.height(72.dp)) {
            CalcButton("7", { actions.onDigit('7') }, Modifier.weight(1f))
            CalcButton("8", { actions.onDigit('8') }, Modifier.weight(1f))
            CalcButton("9", { actions.onDigit('9') }, Modifier.weight(1f))
            CalcButton(
                "×", { actions.onOperator('×') }, Modifier.weight(1f), enabled = isCalculator,
                containerColor = colors.keyOperatorContainer, contentColor = colors.keyOperatorContent
            )
        }
        KeypadRow(Modifier.height(72.dp)) {
            CalcButton("4", { actions.onDigit('4') }, Modifier.weight(1f))
            CalcButton("5", { actions.onDigit('5') }, Modifier.weight(1f))
            CalcButton("6", { actions.onDigit('6') }, Modifier.weight(1f))
            CalcButton(
                "−", { actions.onOperator('-') }, Modifier.weight(1f), enabled = isCalculator,
                containerColor = colors.keyOperatorContainer, contentColor = colors.keyOperatorContent
            )
        }
        KeypadRow(Modifier.height(72.dp)) {
            CalcButton("1", { actions.onDigit('1') }, Modifier.weight(1f))
            CalcButton("2", { actions.onDigit('2') }, Modifier.weight(1f))
            CalcButton("3", { actions.onDigit('3') }, Modifier.weight(1f))
            CalcButton(
                "+", { actions.onOperator('+') }, Modifier.weight(1f), enabled = isCalculator,
                containerColor = colors.keyOperatorContainer, contentColor = colors.keyOperatorContent
            )
        }
        KeypadRow(Modifier.height(72.dp)) {
            CalcButton("()", actions.onParenthesis, Modifier.weight(1f), enabled = isCalculator)
            CalcButton("0", { actions.onDigit('0') }, Modifier.weight(1f))
            CalcButton(".", actions.onDecimal, Modifier.weight(1f))
            CalcButton(
                "=", actions.onEquals, Modifier.weight(1f), enabled = isCalculator,
                containerColor = colors.keyEqualsContainer, contentColor = colors.keyEqualsContent
            )
        }
    }
}

@Composable
private fun KeypadRow(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(6.dp), content = content)
}
