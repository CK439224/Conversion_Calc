package com.numconversion.app.ui.theme

import androidx.annotation.StringRes
import com.numconversion.app.R

/** Whether the app follows the system's light/dark setting or overrides it. */
enum class ThemeMode(val id: String, @StringRes val displayNameRes: Int) {
    SYSTEM(id = "system", displayNameRes = R.string.theme_mode_system),
    LIGHT(id = "light", displayNameRes = R.string.theme_mode_light),
    DARK(id = "dark", displayNameRes = R.string.theme_mode_dark);

    companion object {
        val Default = SYSTEM

        fun fromId(id: String?): ThemeMode = entries.find { it.id == id } ?: Default
    }
}
