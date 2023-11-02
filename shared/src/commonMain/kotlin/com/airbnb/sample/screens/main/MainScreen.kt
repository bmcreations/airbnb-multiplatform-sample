package com.airbnb.sample.screens.main

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.airbnb.sample.navigation.Tabs
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.utils.ui.navigationBars
import com.airbnb.sample.utils.ui.statusBars

@Composable
internal fun RenderMain() {
    // TODO: check for authenticated state
    val tabs = remember { Tabs.anonymous }


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
                    TabBar(tabs)
                }
            ) {
                CurrentTab()
            }
            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .zIndex(999F)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}

@Composable
internal fun TabBar(tabs: List<Tab>) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        elevation = MaterialTheme.dimens.staticGrid.x2,
    ) {
        tabs.forEach { tab ->
            TabNavigationItem(tab)
        }
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