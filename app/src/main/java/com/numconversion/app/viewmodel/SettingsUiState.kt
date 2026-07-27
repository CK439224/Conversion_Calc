package com.numconversion.app.viewmodel

import com.numconversion.app.domain.conversion.FractionPrecision
import com.numconversion.app.ui.theme.ColorPalette
import com.numconversion.app.ui.theme.ThemeMode

/** Every user-configurable app preference shown in the Settings dialog. */
data class AppSettings(
    val palette: ColorPalette = ColorPalette.Default,
    val themeMode: ThemeMode = ThemeMode.Default,
    val hapticsEnabled: Boolean = true,
    val fractionPrecision: FractionPrecision = FractionPrecision.Default
)

/**
 * State for the Settings dialog. [preview] holds a draft of every setting the user has touched
 * but not yet applied — the whole dialog is a single transaction, so a tap on any row (palette,
 * theme mode, haptics, fraction precision) re-themes/re-behaves the app instantly via [effective],
 * and only Apply commits [preview] into [applied] (and persists it); Cancel/dismiss discards it.
 */
data class SettingsUiState(
    val applied: AppSettings = AppSettings(),
    val preview: AppSettings? = null
) {
    val effective: AppSettings get() = preview ?: applied
    val hasPendingChange: Boolean get() = preview != null && preview != applied
}
