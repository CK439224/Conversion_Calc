package com.numconversion.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val AuroraTealLight = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    background = Background,
    surface = Surface,
    error = Error
)

private val AuroraTealDark = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    secondary = SecondaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorDark
)

private val OceanBlueLight = lightColorScheme(
    primary = OceanBluePrimary,
    onPrimary = OceanBlueOnPrimary,
    secondary = OceanBlueSecondary,
    background = OceanBlueBackground,
    surface = Surface,
    error = Error
)

private val OceanBlueDark = darkColorScheme(
    primary = OceanBluePrimaryDark,
    onPrimary = OceanBlueOnPrimaryDark,
    secondary = OceanBlueSecondaryDark,
    background = OceanBlueBackgroundDark,
    surface = OceanBlueSurfaceDark,
    error = ErrorDark
)

private val RoyalVioletLight = lightColorScheme(
    primary = RoyalVioletPrimary,
    onPrimary = RoyalVioletOnPrimary,
    secondary = RoyalVioletSecondary,
    background = RoyalVioletBackground,
    surface = Surface,
    error = Error
)

private val RoyalVioletDark = darkColorScheme(
    primary = RoyalVioletPrimaryDark,
    onPrimary = RoyalVioletOnPrimaryDark,
    secondary = RoyalVioletSecondaryDark,
    background = RoyalVioletBackgroundDark,
    surface = RoyalVioletSurfaceDark,
    error = ErrorDark
)

private val SunsetAmberLight = lightColorScheme(
    primary = SunsetAmberPrimary,
    onPrimary = SunsetAmberOnPrimary,
    secondary = SunsetAmberSecondary,
    background = SunsetAmberBackground,
    surface = Surface,
    error = Error
)

private val SunsetAmberDark = darkColorScheme(
    primary = SunsetAmberPrimaryDark,
    onPrimary = SunsetAmberOnPrimaryDark,
    secondary = SunsetAmberSecondaryDark,
    background = SunsetAmberBackgroundDark,
    surface = SunsetAmberSurfaceDark,
    error = ErrorDark
)

private val CrimsonRoseLight = lightColorScheme(
    primary = CrimsonRosePrimary,
    onPrimary = CrimsonRoseOnPrimary,
    secondary = CrimsonRoseSecondary,
    background = CrimsonRoseBackground,
    surface = Surface,
    error = Error
)

private val CrimsonRoseDark = darkColorScheme(
    primary = CrimsonRosePrimaryDark,
    onPrimary = CrimsonRoseOnPrimaryDark,
    secondary = CrimsonRoseSecondaryDark,
    background = CrimsonRoseBackgroundDark,
    surface = CrimsonRoseSurfaceDark,
    error = ErrorDark
)

/**
 * Each [ColorPalette] is a deliberately designed identity with its own light/dark pair — never
 * swapped out for the device's wallpaper-derived Material You colors.
 */
@Composable
fun NumConversionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    palette: ColorPalette = ColorPalette.Default,
    content: @Composable () -> Unit
) {
    val colorScheme = when (palette) {
        ColorPalette.AURORA_TEAL -> if (darkTheme) AuroraTealDark else AuroraTealLight
        ColorPalette.OCEAN_BLUE -> if (darkTheme) OceanBlueDark else OceanBlueLight
        ColorPalette.ROYAL_VIOLET -> if (darkTheme) RoyalVioletDark else RoyalVioletLight
        ColorPalette.SUNSET_AMBER -> if (darkTheme) SunsetAmberDark else SunsetAmberLight
        ColorPalette.CRIMSON_ROSE -> if (darkTheme) CrimsonRoseDark else CrimsonRoseLight
    }
    val calculatorColors = when (palette) {
        ColorPalette.AURORA_TEAL -> if (darkTheme) DarkCalculatorColors else LightCalculatorColors
        ColorPalette.OCEAN_BLUE -> if (darkTheme) DarkOceanBlueCalculatorColors else LightOceanBlueCalculatorColors
        ColorPalette.ROYAL_VIOLET -> if (darkTheme) DarkRoyalVioletCalculatorColors else LightRoyalVioletCalculatorColors
        ColorPalette.SUNSET_AMBER -> if (darkTheme) DarkSunsetAmberCalculatorColors else LightSunsetAmberCalculatorColors
        ColorPalette.CRIMSON_ROSE -> if (darkTheme) DarkCrimsonRoseCalculatorColors else LightCrimsonRoseCalculatorColors
    }

    CompositionLocalProvider(LocalCalculatorColors provides calculatorColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
