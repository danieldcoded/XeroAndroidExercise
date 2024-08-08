package com.xero.interview.bankrecmatchmaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = BankRecMatchmakerDarkColors.ColorPrimary,
    primaryVariant = BankRecMatchmakerDarkColors.ColorPrimaryDark,
    secondary = BankRecMatchmakerDarkColors.ColorAccent,
    surface = BankRecMatchmakerDarkColors.White,
    onSurface = BankRecMatchmakerDarkColors.TextRegular,
    onPrimary = BankRecMatchmakerDarkColors.White,
    onSecondary = BankRecMatchmakerDarkColors.White
)

private val LightColorPalette = lightColors(
    primary = BankRecMatchmakerLightColors.ColorPrimary,
    primaryVariant = BankRecMatchmakerLightColors.ColorPrimaryDark,
    secondary = BankRecMatchmakerLightColors.ColorAccent,
    surface = BankRecMatchmakerLightColors.White,
    onSurface = BankRecMatchmakerLightColors.TextRegular,
    onPrimary = BankRecMatchmakerLightColors.White,
    onSecondary = BankRecMatchmakerLightColors.White
)

@Composable
fun BankRecMatchmakerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
