package com.airbnb.sample.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal val DarkColorPalette = darkColorScheme(
    primary = Color(0xFFFF385C),
    onPrimary = Color.Black,
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF3700B3)
)

internal val LightColorPalette = lightColorScheme(
    primary = Color(0xFFFF5A5F),
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF3700B3)
)