package com.airbnb.sample.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.airbnb.sample.screens.explore.RenderExplore
import com.airbnb.sample.screens.inbox.RenderInbox
import com.airbnb.sample.screens.login.RenderLogin
import com.airbnb.sample.screens.main.RenderMain
import com.airbnb.sample.screens.profile.RenderProfile
import com.airbnb.sample.screens.settings.RenderSettings
import com.airbnb.sample.screens.trips.RenderTrips
import com.airbnb.sample.screens.wishlist.RenderWishlists

sealed interface Screens : ScreenProvider {
    data object Main : Screen {
        override val key = uniqueScreenKey

        @Composable
        override fun Content() {
            RenderMain()
        }

        data object Explore : Screen {
            override val key = uniqueScreenKey

            @Composable
            override fun Content() = RenderExplore()
        }

        data object Wishlists: Screen {
            override val key = uniqueScreenKey
            @Composable
            override fun Content() = RenderWishlists()
        }

        data object Trips: Screen {
            override val key = uniqueScreenKey
            @Composable
            override fun Content() = RenderTrips()
        }

        data object Inbox: Screen {
            override val key = uniqueScreenKey
            @Composable
            override fun Content() = RenderInbox()
        }

        data object Profile: Screen {
            override val key = uniqueScreenKey
            @Composable
            override fun Content() = RenderProfile()
        }
    }


    data object Settings: Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = RenderSettings()
    }

    data object LoginModal : Screen {
        override val key = uniqueScreenKey
        @Composable
        override fun Content() = RenderLogin()
    }
}