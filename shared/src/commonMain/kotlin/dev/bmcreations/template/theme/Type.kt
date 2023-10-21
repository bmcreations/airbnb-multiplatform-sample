package dev.bmcreations.template.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun typography(
    fontScale: Float,
) = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 64.sp * fontScale,
        lineHeight = 64.8.sp * fontScale,
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 34.sp * fontScale,
        lineHeight = 33.42.sp * fontScale,
        letterSpacing = (-0.3).sp,
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 25.sp * fontScale,
        lineHeight = 34.48.sp * fontScale,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 28.sp * fontScale,
        lineHeight = 33.6.sp * fontScale,
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 24.sp * fontScale,
        lineHeight = 31.66.sp * fontScale,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 18.sp * fontScale,
        lineHeight = 23.74.sp * fontScale,
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 16.sp * fontScale,
        lineHeight = 21.1.sp * fontScale,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 16.sp * fontScale,
        lineHeight = 21.1.sp * fontScale,
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 14.sp * fontScale,
        lineHeight = 14.sp * fontScale,
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 12.sp * fontScale,
        lineHeight = 12.sp * fontScale,
    ),
)