package com.airbnb.sample.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.airbnb.sample.screens.home.Render
import com.airbnb.sample.screens.login.Render

sealed interface Screens : ScreenProvider {
    data object Home : Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = Render()
    }

    data object Login : Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = Render()
    }

    companion object {
        // Navigator loads lastItem as "current screen" when setting up
        val graph = listOf(Home, Login)
    }
}