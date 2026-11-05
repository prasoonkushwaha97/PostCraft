package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Professional Dark-first Theme with Deep Zinc / Slate (#09090b) and Safety Orange
private val DarkColorScheme = darkColorScheme(
    primary = SafetyOrange,
    onPrimary = Color.White,
    primaryContainer = SafetyOrangeDark,
    onPrimaryContainer = Zinc50,
    secondary = ForestTeal,
    onSecondary = Color.White,
    secondaryContainer = ForestTealContainer,
    onSecondaryContainer = Zinc50,
    background = Zinc950,
    surface = Zinc900,
    surfaceVariant = Zinc850,
    onBackground = Zinc100,
    onSurface = Zinc100,
    onSurfaceVariant = Zinc400,
    outline = Zinc700,
    outlineVariant = Zinc800
)

// Clean light scheme (if explicitly toggled, keeping high contrast)
private val LightColorScheme = darkColorScheme( // We enforce dark-first high-contrast mobile theme for GramAwaz
    primary = SafetyOrange,
    onPrimary = Color.White,
    primaryContainer = SafetyOrangeDark,
    onPrimaryContainer = Zinc50,
    secondary = ForestTeal,
    onSecondary = Color.White,
    secondaryContainer = ForestTealContainer,
    onSecondaryContainer = Zinc50,
    background = Zinc950,
    surface = Zinc900,
    surfaceVariant = Zinc850,
    onBackground = Zinc100,
    onSurface = Zinc100,
    onSurfaceVariant = Zinc400,
    outline = Zinc700,
    outlineVariant = Zinc800
)

@Composable
fun GramAwazTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
