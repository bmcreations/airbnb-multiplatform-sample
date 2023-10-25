package com.airbnb.sample.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.airbnb.sample.screens.login.Render
import com.airbnb.sample.screens.profile.Render

sealed interface Screens : ScreenProvider {

    data object Login : Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = Render()
    }

    data object Profile: Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = Render()
    }
}