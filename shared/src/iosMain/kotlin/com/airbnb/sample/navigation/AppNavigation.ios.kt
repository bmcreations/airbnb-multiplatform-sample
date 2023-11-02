package com.airbnb.sample.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.airbnb.sample.utils.Platform
import com.airbnb.sample.utils.shouldUseSwipeBack

@OptIn(ExperimentalMaterialApi::class)
@Composable
actual fun AppNavHost(content: @Composable () -> Unit) {
    BackdropNavigator(
        modifier = Modifier.fillMaxSize(),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContentColor = MaterialTheme.colorScheme.onSurface,
        sheetShape = MaterialTheme.shapes.large,
        sheetContent = {
            val navigator = remember(it) { PlatformNavigator(it) }
            CompositionLocalProvider(LocalPlatformNavigator provides navigator) {
                CurrentScreen()
            }
        }
    ) {
        val navigator = remember(it) { PlatformNavigator(it) }
        CompositionLocalProvider(LocalPlatformNavigator provides navigator) {
            content()
        }
    }
}

actual class PlatformNavigator(
    private val navigator: BackdropNavigator
) {

    actual val isVisible: Boolean
        get() = navigator.isOpen

    actual val progress: Float
        get() = navigator.progress

    actual var screensNavigator: Navigator? = null

    actual val supportsGestureNavigation: Boolean
        get() = Platform.shouldUseSwipeBack

    actual fun show(screen: Screen) {
        navigator.show(screen)
    }

    actual fun hide() {
        navigator.hide()
    }

    actual fun push(item: Screen) {
        screensNavigator?.push(item)
    }

    actual fun push(items: List<Screen>) {
        screensNavigator?.push(items)
    }

    actual fun replace(item: Screen) {
        screensNavigator?.replace(item)
    }

    actual fun replaceAll(item: Screen) {
        screensNavigator?.replaceAll(item)
    }

    actual fun replaceAll(items: List<Screen>) {
        screensNavigator?.replaceAll(items)
    }

    actual fun pop(): Boolean {
        return screensNavigator?.pop() ?: false
    }

    actual fun popAll() {
        screensNavigator?.popAll()
    }

    actual fun popUntil(predicate: (Screen) -> Boolean): Boolean {
        return screensNavigator?.popUntil(predicate) ?: false
    }
}