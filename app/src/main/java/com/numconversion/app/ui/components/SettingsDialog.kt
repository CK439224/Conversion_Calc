package com.numconversion.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.annotation.StringRes
import com.numconversion.app.R
import com.numconversion.app.domain.conversion.FractionPrecision
import com.numconversion.app.ui.theme.ColorPalette
import com.numconversion.app.ui.theme.ThemeMode
import com.numconversion.app.viewmodel.AppSettings

/**
 * The whole dialog is a single transaction: every control below shows [settings] (the live
 * preview) and re-themes/re-behaves the app instantly as the user touches it. Only [onApply]
 * persists the result; dismissing any other way (Cancel, back, outside tap) must discard it, so
 * every exit path is routed through [onDismiss].
 */
@Composable
fun SettingsDialog(
    settings: AppSettings,
    onPaletteSelected: (ColorPalette) -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onHapticsToggled: (Boolean) -> Unit,
    onFractionPrecisionSelected: (FractionPrecision) -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.extraLarge, tonalElevation = 6.dp) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(stringResource(R.string.settings_title), style = MaterialTheme.typography.headlineSmall)

                SectionHeader(R.string.settings_color_palette)
                ColorPalette.entries.forEach { palette ->
                    PaletteOptionRow(
                        palette = palette,
                        selected = palette == settings.palette,
                        onSelect = { onPaletteSelected(palette) }
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                SectionHeader(R.string.settings_theme_mode)
                ThemeMode.entries.forEach { mode ->
                    RadioTextRow(
                        text = stringResource(mode.displayNameRes),
                        selected = mode == settings.themeMode,
                        onSelect = { onThemeModeSelected(mode) },
                        testTag = "theme_mode_option_${mode.id}"
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                SectionHeader(R.string.settings_fraction_precision)
                FractionPrecision.entries.forEach { precision ->
                    RadioTextRow(
                        text = stringResource(precision.displayNameRes),
                        selected = precision == settings.fractionPrecision,
                        onSelect = { onFractionPrecisionSelected(precision) },
                        testTag = "fraction_precision_option_${precision.id}"
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.settings_haptics), style = MaterialTheme.typography.bodyLarge)
                        Text(
                            stringResource(R.string.settings_haptics_description),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    val hapticsDescription = stringResource(R.string.cd_haptics_toggle)
                    Switch(
                        checked = settings.hapticsEnabled,
                        onCheckedChange = onHapticsToggled,
                        modifier = Modifier
                            .testTag("haptics_toggle")
                            .semantics { contentDescription = hapticsDescription }
                    )
                }

                TextButton(
                    onClick = onPrivacyPolicyClick,
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(stringResource(R.string.settings_privacy_policy_link))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) }
                    TextButton(onClick = onApply) { Text(stringResource(R.string.action_apply)) }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(@StringRes textRes: Int) {
    Text(
        stringResource(textRes),
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(top = 20.dp, bottom = 4.dp)
    )
}

@Composable
private fun PaletteOptionRow(
    palette: ColorPalette,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
            .testTag("palette_option_${palette.id}")
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Surface(
            shape = CircleShape,
            color = palette.swatchColor,
            modifier = Modifier.padding(start = 4.dp).size(28.dp)
        ) {}
        Text(
            text = stringResource(palette.displayNameRes),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun RadioTextRow(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
            .testTag(testTag)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
