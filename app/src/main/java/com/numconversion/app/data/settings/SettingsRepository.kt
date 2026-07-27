package com.numconversion.app.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.numconversion.app.domain.conversion.FractionPrecision
import com.numconversion.app.domain.conversion.MeasurementUnit
import com.numconversion.app.ui.theme.ColorPalette
import com.numconversion.app.ui.theme.ThemeMode
import com.numconversion.app.viewmodel.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

/**
 * Persists user preferences across app restarts: the four Settings-dialog options as one
 * [AppSettings] snapshot, plus the converter's last-used unit pair (persisted silently on every
 * change, with no dialog UI of its own — unlike the other four, there's nothing to preview/apply).
 */
class SettingsRepository(private val context: Context) {

    private val colorPaletteKey = stringPreferencesKey("color_palette")
    private val themeModeKey = stringPreferencesKey("theme_mode")
    private val hapticsEnabledKey = booleanPreferencesKey("haptics_enabled")
    private val fractionPrecisionKey = stringPreferencesKey("fraction_precision")
    private val sourceUnitKey = stringPreferencesKey("last_source_unit")
    private val targetUnitKey = stringPreferencesKey("last_target_unit")

    val appSettings: Flow<AppSettings> = context.settingsDataStore.data.map { prefs ->
        AppSettings(
            palette = ColorPalette.fromId(prefs[colorPaletteKey]),
            themeMode = ThemeMode.fromId(prefs[themeModeKey]),
            hapticsEnabled = prefs[hapticsEnabledKey] ?: true,
            fractionPrecision = FractionPrecision.fromId(prefs[fractionPrecisionKey])
        )
    }

    suspend fun saveAppSettings(settings: AppSettings) {
        context.settingsDataStore.edit { prefs ->
            prefs[colorPaletteKey] = settings.palette.id
            prefs[themeModeKey] = settings.themeMode.id
            prefs[hapticsEnabledKey] = settings.hapticsEnabled
            prefs[fractionPrecisionKey] = settings.fractionPrecision.id
        }
    }

    val lastUnits: Flow<Pair<MeasurementUnit, MeasurementUnit>> = context.settingsDataStore.data.map { prefs ->
        val source = prefs[sourceUnitKey]?.let { runCatching { MeasurementUnit.valueOf(it) }.getOrNull() }
        val target = prefs[targetUnitKey]?.let { runCatching { MeasurementUnit.valueOf(it) }.getOrNull() }
        (source ?: MeasurementUnit.MM) to (target ?: MeasurementUnit.IN)
    }

    suspend fun saveLastUnits(sourceUnit: MeasurementUnit, targetUnit: MeasurementUnit) {
        context.settingsDataStore.edit { prefs ->
            prefs[sourceUnitKey] = sourceUnit.name
            prefs[targetUnitKey] = targetUnit.name
        }
    }
}
