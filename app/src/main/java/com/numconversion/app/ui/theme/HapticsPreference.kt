package com.numconversion.app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

/** Whether keypad presses should fire haptic feedback; set app-wide from the user's settings. */
val LocalHapticsEnabled = staticCompositionLocalOf { true }
