package com.airbnb.sample.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.airbnb.sample.ui.components.BackArrow
import com.airbnb.sample.ui.components.TopAppBar
import com.airbnb.sample.ui.components.TopAppBarTextStyle

@Composable
expect fun AppNavHost(content: @Composable () -> Unit)

expect class PlatformNavigator {
    val isVisible: Boolean
    val progress: Float

    val supportsGestureNavigation: Boolean

    var screensNavigator: Navigator?

    fun show(screen: Screen)
    fun hide()
    fun push(item: Screen)

    fun push(items: List<Screen>)

    fun replace(item: Screen)

    fun replaceAll(item: Screen)

    fun replaceAll(items: List<Screen>)

    fun pop(): Boolean

    fun popAll()

    fun popUntil(predicate: (Screen) -> Boolean): Boolean
}


val LocalPlatformNavigator: ProvidableCompositionLocal<PlatformNavigator> =
    staticCompositionLocalOf { error("PlatformNavigator not initialized") }

@Composable
fun AppNavigation() {
    AppNavHost {
        Navigator(Screens.Main) { navigator ->
            val platformNavigator = LocalPlatformNavigator.current
            LaunchedEffect(navigator.lastItem) {
                // update global navigator for platform access to support push/pop from a single
                // navigator current
                platformNavigator.screensNavigator = navigator
            }


            SharedAppScaffolding(navigator) {
                // TODO support swipe back on iOS
                SlideTransition(navigator)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SharedAppScaffolding(
    navigator: Navigator,
    content: @Composable () -> Unit
) {
    val showTopBar by remember(navigator.lastItem) {
        derivedStateOf { navigator.lastItem != Screens.Main }
    }
    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = showTopBar,
                enter = slideInHorizontally() + fadeIn()
            ) {
                TopAppBar(
                    navigationIcon = { BackArrow(onClick = { navigator.pop() } ) },
                    title = {
                        Text(
                            text = navigator.lastItem::class.simpleName.orEmpty(),
                            style = MaterialTheme.typography.TopAppBarTextStyle,
                        )
                    }
                )
            }
        },
        content = {
            val topPadding by animateDpAsState(
                if (showTopBar) it.calculateTopPadding() else 0.dp)

            Box(modifier = Modifier.padding(top = topPadding)) {
                content()
            }
        }
    )
}