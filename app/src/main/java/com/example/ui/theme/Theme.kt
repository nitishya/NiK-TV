package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NikPrimary,
    onPrimary = NikOnPrimary,
    primaryContainer = NikPrimaryVariant,
    secondary = NikSecondary,
    tertiary = NikTertiary,
    background = NikBackground,
    surface = NikSurface,
    surfaceVariant = NikSurfaceVariant,
    onBackground = NikTextPrimary,
    onSurface = NikTextPrimary,
    onSurfaceVariant = NikTextSecondary,
    outline = NikBorder,
    error = NikError
)

@Composable
fun NiKTVTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    NiKTVTheme(darkTheme = darkTheme, content = content)
}
