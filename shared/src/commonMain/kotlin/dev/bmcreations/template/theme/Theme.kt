package dev.bmcreations.template.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    // TODO: expect/actual this for iOS vs Android
//    val screenWidthDp = LocalConfiguration.current.screenWidthDp
//    val screenWidthSizeClass = when {
//        screenWidthDp <= 320 -> WindowSizeClass.COMPACT // compact phone (we device from above document to have more granular control over what we deem as 'compact')
//        screenWidthDp <= 600 -> WindowSizeClass.NORMAL // our defacto phone threshold
//        screenWidthDp <= 840 -> WindowSizeClass.MEDIUM // tablets (either in a resized window or smaller tablet) OR large unfolded inner displays
//        else -> WindowSizeClass.LARGE // tablets at full width (non resized/multi window) OR large unfolded inner displays in landscape
//    }
//    val screenHeightDp = LocalConfiguration.current.screenHeightDp
//    val screenHeightSizeClass = when {
//        screenHeightDp <= 480 -> WindowSizeClass.COMPACT // compact phone
//        screenHeightDp <= 900 -> WindowSizeClass.NORMAL // our defacto phone threshold
//        else -> WindowSizeClass.LARGE
//    }
//
//    if (logDimensions) {
//        Timber.d("dimens:: width = { dp: $screenWidthDp, class: ${screenWidthSizeClass.name} }, height = { dp:$screenHeightDp, class: ${screenHeightSizeClass.name} }")
//    }

//    val dimens = MHDimensions(
//        screenWidth = screenWidthDp.dp,
//        screenHeight = screenHeightDp.dp,
//        inset = when (screenWidthSizeClass) {
//            WindowSizeClass.COMPACT -> 10.dp
//            WindowSizeClass.NORMAL -> 20.dp
//            WindowSizeClass.MEDIUM,
//            WindowSizeClass.LARGE,
//            -> 30.dp
//        },
//        grid = when (screenWidthSizeClass) {
//            WindowSizeClass.COMPACT -> GridDimensionSet(
//                2.dp,
//                4.dp,
//                6.dp,
//                8.dp,
//                10.dp,
//                12.dp,
//                14.dp,
//                16.dp,
//                18.dp,
//                20.dp,
//                22.dp,
//                24.dp,
//                26.dp,
//                28.dp,
//                30.dp,
//                34.dp,
//            )
//
//            WindowSizeClass.NORMAL -> GridDimensionSet(
//                4.dp,
//                8.dp,
//                12.dp,
//                16.dp,
//                20.dp,
//                24.dp,
//                28.dp,
//                32.dp,
//                36.dp,
//                40.dp,
//                44.dp,
//                48.dp,
//                52.dp,
//                56.dp,
//                60.dp,
//                64.dp,
//            )
//
//            WindowSizeClass.MEDIUM,
//            WindowSizeClass.LARGE,
//            -> GridDimensionSet(
//                8.dp,
//                16.dp,
//                24.dp,
//                32.dp,
//                40.dp,
//                48.dp,
//                56.dp,
//                64.dp,
//                72.dp,
//                80.dp,
//                88.dp,
//                96.dp,
//                104.dp,
//                112.dp,
//                120.dp,
//                124.dp,
//            )
//        },
//        widthWindowSizeClass = screenWidthSizeClass,
//        heightWindowSizeClass = screenHeightSizeClass,
//    )
    ProvideDimens(
        dimensions = Dimensions(
            inset = 16.dp,
            grid = GridDimensionSet(
                4.dp,
                8.dp,
                12.dp,
                16.dp,
                20.dp,
                24.dp,
                28.dp,
                32.dp,
                36.dp,
                40.dp,
                44.dp,
                48.dp,
                52.dp,
                56.dp,
                60.dp,
                64.dp,
            )
        )
    ) {

    }
    MaterialTheme(
        colorScheme = colors,
        typography = typography(1f),
        shapes = shapes,
        content = content
    )
}
