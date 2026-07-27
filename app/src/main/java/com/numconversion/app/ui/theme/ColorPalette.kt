package com.numconversion.app.ui.theme

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.numconversion.app.R

/**
 * The five selectable color identities for the app. [swatchColor] is what the settings dialog
 * shows as each option's preview chip; [id] is the DataStore-persisted key, kept stable and
 * separate from the enum name so a future rename of the constant doesn't lose a saved choice.
 */
enum class ColorPalette(val id: String, val swatchColor: Color, @StringRes val displayNameRes: Int) {
    AURORA_TEAL(id = "aurora_teal", swatchColor = Primary, displayNameRes = R.string.palette_aurora_teal),
    OCEAN_BLUE(id = "ocean_blue", swatchColor = OceanBluePrimary, displayNameRes = R.string.palette_ocean_blue),
    ROYAL_VIOLET(id = "royal_violet", swatchColor = RoyalVioletPrimary, displayNameRes = R.string.palette_royal_violet),
    SUNSET_AMBER(id = "sunset_amber", swatchColor = SunsetAmberPrimary, displayNameRes = R.string.palette_sunset_amber),
    CRIMSON_ROSE(id = "crimson_rose", swatchColor = CrimsonRosePrimary, displayNameRes = R.string.palette_crimson_rose);

    companion object {
        val Default = AURORA_TEAL

        fun fromId(id: String?): ColorPalette = entries.find { it.id == id } ?: Default
    }
}
