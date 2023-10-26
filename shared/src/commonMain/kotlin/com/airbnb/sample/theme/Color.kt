package com.airbnb.sample.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// unused for Airbnb as of 10/26/2023
internal val DarkColorPalette = darkColorScheme(
    primary = Color(0xFFFF385C),
    onPrimary = Color.Black,
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF3700B3),
    outline = Color(0xFF9B9FA8),
    outlineVariant = Color(0xFF4B4B4B)
)

internal val LightColorPalette = lightColorScheme(
    primary = Color(0xFFF22B54),
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    surfaceVariant = Color.White,
    background = Color.White,
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF008488),
    outline = Color(0xFFC4C4C4),
    outlineVariant = Color(0xFFF2F2F2)
)


val ColorScheme.primarySecondary: Color
    @Composable get() = Color(0xFFDE1360)
val ColorScheme.primaryTertiary: Color
    @Composable get() = Color(0xFFBF1C5A)
val ColorScheme.primaryVariant: Color
    @Composable get() = Color(0xFFE41E58)

val ColorScheme.fullOutline: Color
    @Composable get() = dayNightColor(
        light = MaterialTheme.colorScheme.onBackground,
        dark = MaterialTheme.colorScheme.onBackground.copy(
            ContentAlpha.medium,
        )
    )

val ColorScheme.secondaryText: Color
    @Composable get() = MaterialTheme.colorScheme.onBackground.copy(alpha = ContentAlpha.medium)

val ColorScheme.primaryToPrimaryVariantGradient: Brush
    @Composable get() = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryVariant
        )
    )

val ColorScheme.primarySecondaryToPrimaryTertiaryGradient: Brush
    @Composable get() = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primarySecondary,
            MaterialTheme.colorScheme.primarySecondary,
            MaterialTheme.colorScheme.primarySecondary,
            MaterialTheme.colorScheme.primaryTertiary
        )
    )

@Composable
fun dayNightColor(light: Color, dark: Color) = if (isSystemInDarkTheme()) dark else light