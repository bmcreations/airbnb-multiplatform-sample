package com.airbnb.sample.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.sample.utils.ui.font


private val inter: FontFamily
    @Composable get() = FontFamily(
    font("Inter", "inter", FontWeight.Normal, FontStyle.Normal)
)

@Composable
fun typography(
    fontScale: Float,
) = Typography(
    displayLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.W600,
        fontSize = 64.sp * fontScale,
        lineHeight = 64.8.sp * fontScale,
    ),
    displayMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.W500,
        fontSize = 34.sp * fontScale,
        lineHeight = 33.42.sp * fontScale,
        letterSpacing = (-0.3).sp,
    ),
    displaySmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.W500,
        fontSize = 25.sp * fontScale,
        lineHeight = 34.48.sp * fontScale,
    ),
    titleMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.W600,
        fontSize = 28.sp * fontScale,
        lineHeight = 33.6.sp * fontScale,
    ),
    titleSmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.W500,
        fontSize = 24.sp * fontScale,
        lineHeight = 31.66.sp * fontScale,
    ),
    bodyLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.W700,
        fontSize = 18.sp * fontScale,
        lineHeight = 23.74.sp * fontScale,
    ),
    bodyMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp * fontScale,
        lineHeight = 21.1.sp * fontScale,
    ),
    bodySmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp * fontScale,
        lineHeight = 14.sp * fontScale,
    ),
    labelLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp * fontScale,
        lineHeight = 21.1.sp * fontScale,
    ),
    labelSmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp * fontScale,
        lineHeight = 12.sp * fontScale,
    ),
)