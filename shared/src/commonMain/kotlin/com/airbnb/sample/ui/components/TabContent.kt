package com.airbnb.sample.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.transitions.SlideTransition
import org.lighthousegames.logging.logging

@Composable
fun Tab.TabContent(vararg startScreen: Screen) {
    Navigator(screens = startScreen.toList()) { navigator ->
        SlideTransition(navigator) { screen ->
            screen.Content()
        }
    }
}