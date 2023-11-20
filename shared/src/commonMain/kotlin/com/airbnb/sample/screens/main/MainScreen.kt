package com.airbnb.sample.screens.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.airbnb.sample.navigation.Tabs
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.BottomSafeArea
import com.airbnb.sample.utils.ui.navigationBars
import com.airbnb.sample.utils.ui.statusBars

val LocalBottomPadding: ProvidableCompositionLocal<Dp> =
    staticCompositionLocalOf { error("LocalBottomPadding not initialized") }

val LocalBottomNavAnimator: ProvidableCompositionLocal<(Float) -> Unit> =
    staticCompositionLocalOf { error("LocalBottomNavAnimator not initialized") }

@Composable
internal fun RenderMain() {
    // TODO: check for authenticated state
    val tabs = remember { Tabs.anonymous }

    var bottomTranslation by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    CompositionLocalProvider(
        LocalBottomNavAnimator provides { bottomTranslation = it }
    ) {
        TabNavigator(Tabs.Anonymous.Explore) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Scaffold(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .weight(1f),
                    bottomBar = {
                        TabBar(tabs) { bottomTranslation }
                    }
                ) {
                    CompositionLocalProvider(LocalBottomPadding provides it.calculateBottomPadding()) {
                        CurrentTab()
                    }
                }
            }
        }
    }
}

@Composable
private fun TabBar(tabs: List<Tab>, offsetProvider: () -> Float) {
    var height by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val elevation by animateDpAsState(
        offsetProvider() * MaterialTheme.dimens.staticGrid.x2
    )

    Column(
        modifier = Modifier
            .onPlaced { with(density) { height = it.size.height.toDp() } }
            .graphicsLayer { translationY = height.toPx() * (1 - offsetProvider()) },
    ) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            elevation = elevation,
        ) {
            tabs.forEach { tab ->
                TabNavigationItem(tab)
            }
        }
        BottomSafeArea(color = MaterialTheme.colorScheme.background)
    }
}

@Composable
internal fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) },
        label = { Text(text = tab.options.title) },
        selectedContentColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
    )
}