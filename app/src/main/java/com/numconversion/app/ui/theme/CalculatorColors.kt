package com.numconversion.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Color roles Material3's default [MaterialTheme.colorScheme] has no slot for: per-key-type
 * button fills (digit/function vs. operator vs. equals) and the display panel's own surface.
 * Provided via [LocalCalculatorColors] alongside the standard color scheme.
 */
data class CalculatorColors(
    val keyDigitContainer: Color,
    val keyDigitContent: Color,
    val keyOperatorContainer: Color,
    val keyOperatorContent: Color,
    val keyEqualsContainer: Color,
    val keyEqualsContent: Color,
    val displayBackground: Color,
    val displayBorder: Color,
    val displayContent: Color,
    val displayIcon: Color
)

val LightCalculatorColors = CalculatorColors(
    keyDigitContainer = KeyDigitContainer,
    keyDigitContent = KeyDigitContent,
    keyOperatorContainer = KeyOperatorContainer,
    keyOperatorContent = KeyOperatorContent,
    keyEqualsContainer = KeyEqualsContainer,
    keyEqualsContent = KeyEqualsContent,
    displayBackground = DisplayBackground,
    displayBorder = DisplayBorder,
    displayContent = DisplayContent,
    displayIcon = DisplayIcon
)

val DarkCalculatorColors = CalculatorColors(
    keyDigitContainer = KeyDigitContainerDark,
    keyDigitContent = KeyDigitContentDark,
    keyOperatorContainer = KeyOperatorContainerDark,
    keyOperatorContent = KeyOperatorContentDark,
    keyEqualsContainer = KeyEqualsContainerDark,
    keyEqualsContent = KeyEqualsContentDark,
    displayBackground = DisplayBackgroundDark,
    displayBorder = DisplayBorderDark,
    displayContent = DisplayContentDark,
    displayIcon = DisplayIconDark
)

val LightOceanBlueCalculatorColors = CalculatorColors(
    keyDigitContainer = OceanBlueKeyDigitContainer,
    keyDigitContent = OceanBlueKeyDigitContent,
    keyOperatorContainer = OceanBlueKeyOperatorContainer,
    keyOperatorContent = OceanBlueKeyOperatorContent,
    keyEqualsContainer = OceanBlueKeyEqualsContainer,
    keyEqualsContent = OceanBlueKeyEqualsContent,
    displayBackground = Color(0xFFFFFFFF),
    displayBorder = OceanBlueDisplayBorder,
    displayContent = OceanBlueDisplayContent,
    displayIcon = OceanBlueDisplayIcon
)

val DarkOceanBlueCalculatorColors = CalculatorColors(
    keyDigitContainer = OceanBlueKeyDigitContainerDark,
    keyDigitContent = OceanBlueKeyDigitContentDark,
    keyOperatorContainer = OceanBlueKeyOperatorContainerDark,
    keyOperatorContent = OceanBlueKeyOperatorContentDark,
    keyEqualsContainer = OceanBlueKeyEqualsContainerDark,
    keyEqualsContent = OceanBlueKeyEqualsContentDark,
    displayBackground = OceanBlueSurfaceDark,
    displayBorder = OceanBlueDisplayBorderDark,
    displayContent = OceanBlueKeyDigitContentDark,
    displayIcon = OceanBlueDisplayIconDark
)

val LightRoyalVioletCalculatorColors = CalculatorColors(
    keyDigitContainer = RoyalVioletKeyDigitContainer,
    keyDigitContent = RoyalVioletKeyDigitContent,
    keyOperatorContainer = RoyalVioletKeyOperatorContainer,
    keyOperatorContent = RoyalVioletKeyOperatorContent,
    keyEqualsContainer = RoyalVioletKeyEqualsContainer,
    keyEqualsContent = RoyalVioletKeyEqualsContent,
    displayBackground = Color(0xFFFFFFFF),
    displayBorder = RoyalVioletDisplayBorder,
    displayContent = RoyalVioletDisplayContent,
    displayIcon = RoyalVioletDisplayIcon
)

val DarkRoyalVioletCalculatorColors = CalculatorColors(
    keyDigitContainer = RoyalVioletKeyDigitContainerDark,
    keyDigitContent = RoyalVioletKeyDigitContentDark,
    keyOperatorContainer = RoyalVioletKeyOperatorContainerDark,
    keyOperatorContent = RoyalVioletKeyOperatorContentDark,
    keyEqualsContainer = RoyalVioletKeyEqualsContainerDark,
    keyEqualsContent = RoyalVioletKeyEqualsContentDark,
    displayBackground = RoyalVioletSurfaceDark,
    displayBorder = RoyalVioletDisplayBorderDark,
    displayContent = RoyalVioletKeyDigitContentDark,
    displayIcon = RoyalVioletDisplayIconDark
)

val LightSunsetAmberCalculatorColors = CalculatorColors(
    keyDigitContainer = SunsetAmberKeyDigitContainer,
    keyDigitContent = SunsetAmberKeyDigitContent,
    keyOperatorContainer = SunsetAmberKeyOperatorContainer,
    keyOperatorContent = SunsetAmberKeyOperatorContent,
    keyEqualsContainer = SunsetAmberKeyEqualsContainer,
    keyEqualsContent = SunsetAmberKeyEqualsContent,
    displayBackground = Color(0xFFFFFFFF),
    displayBorder = SunsetAmberDisplayBorder,
    displayContent = SunsetAmberDisplayContent,
    displayIcon = SunsetAmberDisplayIcon
)

val DarkSunsetAmberCalculatorColors = CalculatorColors(
    keyDigitContainer = SunsetAmberKeyDigitContainerDark,
    keyDigitContent = SunsetAmberKeyDigitContentDark,
    keyOperatorContainer = SunsetAmberKeyOperatorContainerDark,
    keyOperatorContent = SunsetAmberKeyOperatorContentDark,
    keyEqualsContainer = SunsetAmberKeyEqualsContainerDark,
    keyEqualsContent = SunsetAmberKeyEqualsContentDark,
    displayBackground = SunsetAmberSurfaceDark,
    displayBorder = SunsetAmberDisplayBorderDark,
    displayContent = SunsetAmberKeyDigitContentDark,
    displayIcon = SunsetAmberDisplayIconDark
)

val LightCrimsonRoseCalculatorColors = CalculatorColors(
    keyDigitContainer = CrimsonRoseKeyDigitContainer,
    keyDigitContent = CrimsonRoseKeyDigitContent,
    keyOperatorContainer = CrimsonRoseKeyOperatorContainer,
    keyOperatorContent = CrimsonRoseKeyOperatorContent,
    keyEqualsContainer = CrimsonRoseKeyEqualsContainer,
    keyEqualsContent = CrimsonRoseKeyEqualsContent,
    displayBackground = Color(0xFFFFFFFF),
    displayBorder = CrimsonRoseDisplayBorder,
    displayContent = CrimsonRoseDisplayContent,
    displayIcon = CrimsonRoseDisplayIcon
)

val DarkCrimsonRoseCalculatorColors = CalculatorColors(
    keyDigitContainer = CrimsonRoseKeyDigitContainerDark,
    keyDigitContent = CrimsonRoseKeyDigitContentDark,
    keyOperatorContainer = CrimsonRoseKeyOperatorContainerDark,
    keyOperatorContent = CrimsonRoseKeyOperatorContentDark,
    keyEqualsContainer = CrimsonRoseKeyEqualsContainerDark,
    keyEqualsContent = CrimsonRoseKeyEqualsContentDark,
    displayBackground = CrimsonRoseSurfaceDark,
    displayBorder = CrimsonRoseDisplayBorderDark,
    displayContent = CrimsonRoseKeyDigitContentDark,
    displayIcon = CrimsonRoseDisplayIconDark
)

val LocalCalculatorColors = staticCompositionLocalOf { LightCalculatorColors }

val MaterialTheme.calculatorColors: CalculatorColors
    @Composable
    get() = LocalCalculatorColors.current
