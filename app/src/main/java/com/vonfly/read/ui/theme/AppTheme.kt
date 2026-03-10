package com.vonfly.read.ui.theme

import androidx.compose.material3.MaterialTheme
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

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
