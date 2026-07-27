package com.numconversion.app.ui.calculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.numconversion.app.ui.components.AnswerDisplay
import com.numconversion.app.ui.components.DisplayCard
import com.numconversion.app.ui.components.KeypadActions
import com.numconversion.app.ui.components.KeypadGrid
import com.numconversion.app.ui.components.KeypadMode
import com.numconversion.app.viewmodel.MainViewModel

@Composable
fun CalculatorScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.calculatorState.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        DisplayCard(modifier = Modifier.fillMaxWidth()) {
            AnswerDisplay(
                text = state.display,
                isError = state.hasError,
                modifier = Modifier.padding(16.dp),
                testTag = "calculatorDisplay"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            KeypadGrid(
                mode = KeypadMode.CALCULATOR,
                actions = KeypadActions(
                    onDigit = viewModel::onCalculatorDigit,
                    onDecimal = viewModel::onCalculatorDecimal,
                    onClear = viewModel::onCalculatorClear,
                    onBackspace = viewModel::onCalculatorBackspace,
                    onOperator = viewModel::onCalculatorOperator,
                    onParenthesis = viewModel::onCalculatorParenthesis,
                    onFraction = viewModel::onCalculatorFractionKey,
                    onEquals = viewModel::onCalculatorEquals
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
