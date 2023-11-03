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
import com.airbnb.sample.screens.settings.currency.RenderCurrencySelection
import com.airbnb.sample.screens.settings.RenderSettings
import com.airbnb.sample.screens.profile.a11y.RenderAccessibility
import com.airbnb.sample.screens.trips.RenderTrips
import com.airbnb.sample.screens.web.WebScreen
import com.airbnb.sample.screens.wishlist.RenderWishlists

sealed interface Screens : ScreenProvider {

    sealed interface Web {
        val webUrl: String
    }

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

            data object Accessibility: Screen {
                override val key = "Accessibility"
                @Composable
                override fun Content() = RenderAccessibility()
            }

            data object HelpCenter: Screen, Web {
                override val key = "Help Center"

                override val webUrl: String
                    get() = "https://www.airbnb.com/help/?audience=guest"
                @Composable
                override fun Content() = WebScreen(this)
            }
        }
    }


    data object Settings: Screen {
        override val key = "Settings"
        @Composable
        override fun Content() = RenderSettings()

        data object CurrencySelection: Screen {
            override val key = "Choose a currency"
            @Composable
            override fun Content() = RenderCurrencySelection()
        }

        data object Terms: Screen, Web {
            override val key = "Terms of Service"

            override val webUrl: String
                get() = "https://www.airbnb.com/help/article/2908"
            @Composable
            override fun Content() = WebScreen(this)
        }

        data object PrivacyPolicy: Screen, Web {
            override val key = "Privacy Policy"

            override val webUrl: String
                get() = "https://www.airbnb.com/help/article/2855"
            @Composable
            override fun Content() = WebScreen(this)
        }

        data object PrivacyChoices: Screen, Web {
            override val key = "Your Privacy Choices"

            override val webUrl: String
                get() = "https://www.airbnb.com/help/sale-share-opt-out"
            @Composable
            override fun Content() = WebScreen(this)
        }
    }

    data object LoginModal : Screen {
        override val key = "Log in or sign up"
        @Composable
        override fun Content() = RenderLogin()
    }
}