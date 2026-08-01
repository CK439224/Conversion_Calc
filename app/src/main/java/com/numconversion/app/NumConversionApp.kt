package com.numconversion.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.numconversion.app.data.settings.SettingsRepository
import com.numconversion.app.ui.calculator.CalculatorScreen
import com.numconversion.app.ui.components.AppMode
import com.numconversion.app.ui.components.ModeToggle
import com.numconversion.app.ui.components.PrivacyPolicyDialog
import com.numconversion.app.ui.components.SettingsDialog
import com.numconversion.app.ui.converter.ConverterScreen
import com.numconversion.app.ui.history.HistoryScreen
import com.numconversion.app.ui.theme.LocalHapticsEnabled
import com.numconversion.app.ui.theme.NumConversionTheme
import com.numconversion.app.ui.theme.ThemeMode
import com.numconversion.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumConversionApp() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(
        factory = viewModelFactory {
            initializer { MainViewModel(SettingsRepository(context.applicationContext)) }
        }
    )
    var mode by remember { mutableStateOf(AppMode.CALCULATOR) }
    var showSettings by remember { mutableStateOf(false) }
    var showPrivacyPolicy by remember { mutableStateOf(false) }
    val settingsState by viewModel.settingsState.collectAsState()
    val effective = settingsState.effective

    val useDarkTheme = when (effective.themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    NumConversionTheme(darkTheme = useDarkTheme, palette = effective.palette) {
        CompositionLocalProvider(LocalHapticsEnabled provides effective.hapticsEnabled) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                    TopAppBar(
                        title = { Text(stringResource(R.string.app_name)) },
                        actions = {
                            IconButton(onClick = { showSettings = true }) {
                                Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.cd_settings))
                            }
                        }
                    )
                    ModeToggle(
                        selected = mode,
                        onSelect = { mode = it },
                        tabs = listOf(
                            AppMode.CALCULATOR to stringResource(R.string.mode_calculator),
                            AppMode.CONVERT to stringResource(R.string.mode_convert),
                            AppMode.HISTORY to stringResource(R.string.mode_history)
                        )
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        when (mode) {
                            AppMode.CALCULATOR -> CalculatorScreen(viewModel)
                            AppMode.CONVERT -> ConverterScreen(viewModel)
                            AppMode.HISTORY -> HistoryScreen(viewModel)
                        }
                    }
                }
            }

            if (showSettings) {
                SettingsDialog(
                    settings = effective,
                    onPaletteSelected = viewModel::onPalettePreviewed,
                    onThemeModeSelected = viewModel::onThemeModePreviewed,
                    onHapticsToggled = viewModel::onHapticsPreviewed,
                    onFractionPrecisionSelected = viewModel::onFractionPrecisionPreviewed,
                    onPrivacyPolicyClick = { showPrivacyPolicy = true },
                    onApply = {
                        viewModel.onSettingsApplied()
                        showSettings = false
                    },
                    onDismiss = {
                        viewModel.onSettingsPreviewDiscarded()
                        showSettings = false
                    }
                )
            }

            if (showPrivacyPolicy) {
                PrivacyPolicyDialog(onDismiss = { showPrivacyPolicy = false })
            }
        }
    }
}
