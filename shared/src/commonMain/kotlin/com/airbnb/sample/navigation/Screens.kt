package com.airbnb.sample.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.airbnb.sample.screens.explore.Render
import com.airbnb.sample.screens.inbox.Render
import com.airbnb.sample.screens.login.Render
import com.airbnb.sample.screens.profile.Render
import com.airbnb.sample.screens.settings.Render
import com.airbnb.sample.screens.trips.Render
import com.airbnb.sample.screens.wishlist.Render

sealed interface Screens : ScreenProvider {

    data object Explore: Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = Render()
    }

    data object Wishlists: Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = Render()
    }

    data object Trips: Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = Render()
    }

    data object Inbox: Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = Render()
    }

    data object Settings: Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = Render()
    }

    data object Profile: Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = Render()
    }

    data object LoginModal : Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = Render()
    }
}