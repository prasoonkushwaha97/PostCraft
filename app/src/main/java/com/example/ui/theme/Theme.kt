package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ==============================================================================
// Modern Newsroom Color Scheme
// Sophisticated Navy Blue (#10243A) + Electric Cyan (#20C5D8) + White (#FFFFFF)
// ==============================================================================
private val NewsroomLightColorScheme = lightColorScheme(
    primary = PrimaryNavy,
    onPrimary = Color.White,
    primaryContainer = SecondaryBlue,
    onPrimaryContainer = Color.White,
    secondary = SecondaryBlue,
    onSecondary = Color.White,
    secondaryContainer = AccentCyanSubtle,
    onSecondaryContainer = PrimaryNavy,
    tertiary = AccentCyan,
    onTertiary = PrimaryNavy,
    tertiaryContainer = AccentCyanSubtle,
    onTertiaryContainer = PrimaryNavy,
    background = MainBackground,
    onBackground = PrimaryText,
    surface = CardBackground,
    onSurface = PrimaryText,
    surfaceVariant = CardBackgroundMuted,
    onSurfaceVariant = SecondaryText,
    outline = BorderSubtle,
    outlineVariant = BorderHover,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRedSubtle,
    onErrorContainer = ErrorRed
)

@Composable
fun GramAwazTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NewsroomLightColorScheme,
        typography = Typography,
        content = content
    )
}
