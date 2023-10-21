package dev.bmcreations.template.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal val DarkColorPalette = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF3700B3)
)

internal val LightColorPalette = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF3700B3)
)