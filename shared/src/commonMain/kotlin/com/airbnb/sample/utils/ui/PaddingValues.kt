package com.airbnb.sample.utils.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

@Composable
fun PaddingValues.copy(
    start: Dp = calculateStartPadding(),
    top: Dp = calculateTopPadding(),
    end: Dp = calculateEndPadding(),
    bottom: Dp = calculateBottomPadding()
): PaddingValues {
    val comparison = PaddingValues(start, top, end, bottom)
    if (this == comparison) return this

    return PaddingValues(
        start = start,
        top = top,
        end = end,
        bottom = bottom,
    )
}

@Composable
fun PaddingValues.copy(
    horizontal: Dp = calculateHorizontalPadding(),
    vertical: Dp = calculateVerticalPadding(),
): PaddingValues {
    val comparison = PaddingValues(horizontal, vertical)
    if (this == comparison) return this

    return PaddingValues(
        horizontal, vertical
    )
}

@Composable
operator fun PaddingValues.plus(padding: PaddingValues): PaddingValues {
    val ldr = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(ldr) + padding.calculateStartPadding(ldr),
        top = calculateTopPadding() + padding.calculateTopPadding(),
        end = calculateEndPadding(ldr) + padding.calculateEndPadding(ldr),
        bottom = calculateBottomPadding() + padding.calculateBottomPadding(),
    )
}

@Composable
operator fun PaddingValues.minus(padding: PaddingValues): PaddingValues {
    val ldr = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(ldr) - padding.calculateStartPadding(ldr),
        top = calculateTopPadding() - padding.calculateTopPadding(),
        end = calculateEndPadding(ldr) - padding.calculateEndPadding(ldr),
        bottom = calculateBottomPadding() - padding.calculateBottomPadding(),
    )
}

@Composable
operator fun PaddingValues.times(multiplier: Float): PaddingValues {
    val ldr = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(ldr) * multiplier,
        top = calculateTopPadding() * multiplier,
        end = calculateEndPadding(ldr) * multiplier,
        bottom = calculateBottomPadding() * multiplier,
    )
}

@Composable
operator fun PaddingValues.div(other: Float): PaddingValues {
    return PaddingValues(
        start = calculateStartPadding() / other,
        top = calculateTopPadding() / other,
        end = calculateEndPadding() / other,
        bottom = calculateBottomPadding() / other,
    )
}

@Composable
fun PaddingValues.calculateStartPadding(): Dp {
    val ldr = LocalLayoutDirection.current
    return calculateStartPadding(ldr)
}

@Composable
fun PaddingValues.calculateEndPadding(): Dp {
    val ldr = LocalLayoutDirection.current
    return calculateEndPadding(ldr)
}

@Composable
fun PaddingValues.calculateVerticalPadding(): Dp {
    return calculateTopPadding() + calculateBottomPadding()
}

@Composable
fun PaddingValues.calculateHorizontalPadding(): Dp {
    return calculateStartPadding() + calculateEndPadding()
}
