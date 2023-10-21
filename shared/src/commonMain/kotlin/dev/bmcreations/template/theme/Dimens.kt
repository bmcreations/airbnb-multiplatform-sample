package dev.bmcreations.template.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val LocalDimens = staticCompositionLocalOf<Dimensions> {
    error("No Dimensions provided")
}

// Suppress unused warning about unused receiver. Using as an extension
val MaterialTheme.dimens: Dimensions
    @Composable get() = LocalDimens.current

@Composable
fun ProvideDimens(
    dimensions: Dimensions,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalDimens provides dimensions, content = content)
}

enum class WindowSizeClass { COMPACT, NORMAL, MEDIUM, LARGE }

private val staticGridPreset =
    GridDimensionSet(
        x1 = 4.dp,
        x2 = 8.dp,
        x3 = 12.dp,
        x4 = 16.dp,
        x5 = 20.dp,
        x6 = 24.dp,
        x7 = 28.dp,
        x8 = 32.dp,
        x9 = 36.dp,
        x10 = 40.dp,
        x11 = 44.dp,
        x12 = 48.dp,
        x13 = 52.dp,
        x14 = 56.dp,
        x15 = 60.dp,
        x16 = 64.dp,
    )

class Dimensions(
    val none: Dp = 0.dp,
    val border: Dp = 1.dp,
    val thickBorder: Dp = 2.dp,
    val inset: Dp,
    val screenWidth: Dp = Dp.Unspecified,
    val screenHeight: Dp = Dp.Unspecified,
    val widthWindowSizeClass: WindowSizeClass = WindowSizeClass.NORMAL,
    val heightWindowSizeClass: WindowSizeClass = WindowSizeClass.NORMAL,
    val activityCardHeight: Dp = 512.dp,
    val buildingProfileCardWidthTablet: Dp = 500.dp,
    /**
     * Material design has grid spacings by 4dp increments for normal use cases
     * This field is dynamically sized based on screen size
     */
    val grid: GridDimensionSet,
    /**
     * A static grid that is screen size independent based on Material design 4dp spacing
     */
    val staticGrid: GridDimensionSet = staticGridPreset,
) {
    val isCompactWidth: Boolean
        get() = widthWindowSizeClass == WindowSizeClass.COMPACT

    val isCompactHeight: Boolean
        get() = heightWindowSizeClass == WindowSizeClass.COMPACT

    val isMediumWidth: Boolean
        get() = widthWindowSizeClass == WindowSizeClass.MEDIUM

    val isLargeWidth: Boolean
        get() = widthWindowSizeClass == WindowSizeClass.LARGE

    // if content pane is less than 1/2 of tablet use phone layout, otherwise too squished
    val isSufficientSpaceForMultiPaneContent: Boolean
        @Composable get() = MaterialTheme.dimens.screenWidth / 2 > 400.dp
}

data class GridDimensionSet(
    val x1: Dp,
    val x2: Dp,
    val x3: Dp,
    val x4: Dp,
    val x5: Dp,
    val x6: Dp,
    val x7: Dp,
    val x8: Dp,
    val x9: Dp,
    val x10: Dp,
    val x11: Dp,
    val x12: Dp,
    val x13: Dp,
    val x14: Dp,
    val x15: Dp,
    val x16: Dp,
)