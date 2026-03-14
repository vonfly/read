package com.vonfly.read.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Card,
    primaryContainer = PrimaryLight,
    secondary = ForegroundSecondary,
    tertiary = Accent,
    background = Background,
    surface = Card,
    onBackground = Foreground,
    onSurface = Foreground,
    outline = Border,
    outlineVariant = BorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = CardDark,
    primaryContainer = PrimaryLightDark,
    secondary = ForegroundSecondaryDark,
    tertiary = AccentDark,
    background = BackgroundDark,
    surface = CardDark,
    onBackground = ForegroundDark,
    onSurface = ForegroundDark,
    outline = BorderDark,
    outlineVariant = BorderLightDark
)

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
