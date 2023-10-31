package com.airbnb.sample.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen


@OptIn(ExperimentalMaterialApi::class)
@Composable
actual fun AppNavHost(content: @Composable () -> Unit) {
    BottomSheetNavigator(
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
    private val navigator: BottomSheetNavigator
) {
    actual val isVisible: Boolean
        get() = navigator.isVisible

    actual val progress: Float
        get() = navigator.progress

    actual fun show(screen: Screen) {
        navigator.show(screen)
    }

    actual fun hide() {
        navigator.hide()
    }

    actual fun push(item: Screen) {
        navigator.push(item)
    }

    actual fun push(items: List<Screen>) {
        navigator.push(items)
    }

    actual fun replace(item: Screen) {
        navigator.replace(item)
    }

    actual fun replaceAll(item: Screen) {
        navigator.replaceAll(item)
    }

    actual fun replaceAll(items: List<Screen>) {
        navigator.replaceAll(items)
    }

    actual fun pop(): Boolean {
        return navigator.pop()
    }

    actual fun popAll() {
        navigator.popAll()
    }

    actual fun popUntil(predicate: (Screen) -> Boolean): Boolean {
        return navigator.popUntil(predicate)
    }
}