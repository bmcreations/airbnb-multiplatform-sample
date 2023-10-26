package com.airbnb.sample.utils.ui

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.debugBounds(
    width: Dp = 1.dp,
    color: Color = Color.Magenta,
    shape: Shape = RectangleShape,
) = this.border(width = width, color = color, shape = shape)

fun Modifier.addIf(condition: Boolean, other: @Composable () -> Modifier): Modifier = composed {
    then(if (condition) other() else Modifier)
}

fun <T> Modifier.addIfNonNull(value: T?, other: @Composable (T) -> Modifier): Modifier = composed {
    then(if (value != null) other(value) else Modifier)
}