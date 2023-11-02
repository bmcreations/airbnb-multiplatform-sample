package com.airbnb.sample.navigation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.BackdropScaffoldDefaults
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.Stack
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.compositionUniqueId
import com.airbnb.sample.ui.components.CupertinoBackdropScaffold
import com.airbnb.sample.ui.components.cupertinoAnimationSpec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias BackdropNavigatorContent = @Composable (backdropNavigator: BackdropNavigator) -> Unit

val LocalBackdropNavigator: ProvidableCompositionLocal<BackdropNavigator> =
    staticCompositionLocalOf { error("BackdropNavigator not initialized") }


@OptIn(InternalVoyagerApi::class)
@ExperimentalMaterialApi
@Composable
fun BackdropNavigator(
    modifier: Modifier = Modifier,
    hideOnBackPress: Boolean = true,
    scrimColor: Color = Color.Black.copy(alpha = 1 / 3f),
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = BackdropScaffoldDefaults.FrontLayerElevation,
    sheetBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    sheetGesturesEnabled: Boolean = true,
    animationSpec: AnimationSpec<Float> = BackdropScaffoldDefaults.cupertinoAnimationSpec(),
    key: String = compositionUniqueId(),
    sheetContent: BackdropNavigatorContent = { CurrentScreen() },
    content: BackdropNavigatorContent
) {
    var hideBottomSheet: (() -> Unit)? = null
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(
        initialValue = BackdropValue.Revealed,
        confirmStateChange = { state ->
            if (state == BackdropValue.Concealed) {
                hideBottomSheet?.invoke()
            }
            true
        },
        animationSpec = animationSpec
    )

    Navigator(HiddenBottomSheetScreen, onBackPressed = null, key = key) { navigator ->
        val backdropNavigator = remember(navigator, scaffoldState, coroutineScope) {
            BackdropNavigator(navigator, scaffoldState, coroutineScope)
        }

        hideBottomSheet = backdropNavigator::hide

        CompositionLocalProvider(
            LocalBackdropNavigator provides backdropNavigator,
        ) {
            CupertinoBackdropScaffold(
                modifier = modifier,
                scaffoldState = scaffoldState,
                frontLayerShape = sheetShape,
                frontLayerElevation = sheetElevation,
                frontLayerBackgroundColor = sheetBackgroundColor,
                frontLayerContentColor = sheetContentColor,
                frontLayerScrimColor = scrimColor,
                gesturesEnabled = sheetGesturesEnabled,
                frontLayerContent = {
                    BackdropNavigatorBackHandler(backdropNavigator, scaffoldState, hideOnBackPress)
                    sheetContent(backdropNavigator)
                },
                backLayerContent = {
                    content(backdropNavigator)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
class BackdropNavigator internal constructor(
    private val navigator: Navigator,
    private val scaffoldState: BackdropScaffoldState,
    private val coroutineScope: CoroutineScope
) : Stack<Screen> by navigator {

    val isOpen: Boolean
        get() = scaffoldState.isConcealed

    val progress: Float
        get() = scaffoldState.progress.fraction

    fun show(screen: Screen) {
        coroutineScope.launch {
            replaceAll(screen)
            scaffoldState.conceal()
        }
    }

    fun hide() {
        coroutineScope.launch {
            if (isOpen) {
                scaffoldState.reveal()
                replaceAll(HiddenBottomSheetScreen)
            }
        }
    }

    @Composable
    fun saveableState(
        key: String,
        content: @Composable () -> Unit
    ) {
        navigator.saveableState(key, content = content)
    }
}

private object HiddenBottomSheetScreen : Screen {

    @Composable
    override fun Content() {
        Spacer(modifier = Modifier.height(1.dp))
    }
}

@ExperimentalMaterialApi
@Composable
private fun BackdropNavigatorBackHandler(
    navigator: BackdropNavigator,
    scaffoldState: BackdropScaffoldState,
    hideOnBackPress: Boolean
) {
    BackHandler(enabled = scaffoldState.isRevealed) {
        if (navigator.pop().not() && hideOnBackPress) {
            navigator.hide()
        }
    }
}