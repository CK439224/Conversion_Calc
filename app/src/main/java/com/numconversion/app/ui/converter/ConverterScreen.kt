package com.numconversion.app.ui.converter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.numconversion.app.R
import com.numconversion.app.ui.components.AnswerDisplay
import com.numconversion.app.ui.components.DisplayCard
import com.numconversion.app.ui.components.KeypadActions
import com.numconversion.app.ui.components.KeypadGrid
import com.numconversion.app.ui.components.KeypadMode
import com.numconversion.app.ui.components.UnitDropdown
import com.numconversion.app.viewmodel.ConverterField
import com.numconversion.app.viewmodel.MainViewModel

@Composable
fun ConverterScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.converterState.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        UnitDropdown(
            label = stringResource(R.string.cd_source_unit),
            selected = state.sourceUnit,
            onSelect = viewModel::onSourceUnitChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        if (state.isFeetInchesSource) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.feetInput,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.hint_feet)) },
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { if (it.isFocused) viewModel.onConverterFieldFocused(ConverterField.FEET) }
                )
                OutlinedTextField(
                    value = state.inchesInput,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.hint_inches)) },
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { if (it.isFocused) viewModel.onConverterFieldFocused(ConverterField.INCHES) }
                )
            }
        } else {
            OutlinedTextField(
                value = state.input,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.hint_input)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { if (it.isFocused) viewModel.onConverterFieldFocused(ConverterField.SINGLE) }
            )
        }

        Spacer(Modifier.height(4.dp))
        Text(stringResource(R.string.label_to), style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(4.dp))

        UnitDropdown(
            label = stringResource(R.string.cd_target_unit),
            selected = state.targetUnit,
            onSelect = viewModel::onTargetUnitChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.label_equals), style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(4.dp))

        DisplayCard(modifier = Modifier.fillMaxWidth()) {
            AnswerDisplay(
                text = state.result,
                isError = state.errorMessage != null,
                modifier = Modifier.padding(16.dp),
                onCopied = viewModel::onConverterResultCopied,
                testTag = "converterResult"
            )
        }

        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            KeypadGrid(
                mode = KeypadMode.CONVERTER,
                actions = KeypadActions(
                    onDigit = viewModel::onConverterDigit,
                    onDecimal = viewModel::onConverterDecimal,
                    onClear = viewModel::onConverterClear,
                    onBackspace = viewModel::onConverterBackspace
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
