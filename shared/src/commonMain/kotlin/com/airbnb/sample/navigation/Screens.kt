package com.airbnb.sample.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.airbnb.sample.screens.RenderExplore
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

    sealed interface NamedScreen : Screen {
        val name: String
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

        data object Wishlists : Screen {
            override val key = uniqueScreenKey

            @Composable
            override fun Content() = RenderWishlists()
        }

        data object Trips : Screen {
            override val key = uniqueScreenKey

            @Composable
            override fun Content() = RenderTrips()
        }

        data object Inbox : Screen {
            override val key = uniqueScreenKey

            @Composable
            override fun Content() = RenderInbox()
        }

        data object Profile : Screen {
            override val key = uniqueScreenKey

            @Composable
            override fun Content() = RenderProfile()

            data object Accessibility : NamedScreen {
                override val name = "Accessibility"
                override val key = uniqueScreenKey

                @Composable
                override fun Content() = RenderAccessibility()
            }

            data object HelpCenter : NamedScreen, Web {
                override val name = "Help Center"
                override val key = uniqueScreenKey

                override val webUrl: String
                    get() = "https://www.airbnb.com/help/?audience=guest"

                @Composable
                override fun Content() = WebScreen(this)
            }
        }
    }


    data object Settings : NamedScreen {
        override val name = "Settings"
        override val key = uniqueScreenKey

        @Composable
        override fun Content() = RenderSettings()

        data object CurrencySelection : NamedScreen {
            override val name = "Choose a currency"
            override val key = uniqueScreenKey

            @Composable
            override fun Content() = RenderCurrencySelection()
        }

        data object Terms : NamedScreen, Web {
            override val name = "Terms of Service"
            override val key = uniqueScreenKey
            override val webUrl: String
                get() = "https://www.airbnb.com/help/article/2908"

            @Composable
            override fun Content() = WebScreen(this)
        }

        data object PrivacyPolicy : NamedScreen, Web {
            override val name = "Privacy Policy"
            override val key = uniqueScreenKey

            override val webUrl: String
                get() = "https://www.airbnb.com/help/article/2855"

            @Composable
            override fun Content() = WebScreen(this)
        }

        data object PrivacyChoices : NamedScreen, Web {
            override val name = "Your Privacy Choices"
            override val key = uniqueScreenKey

            override val webUrl: String
                get() = "https://www.airbnb.com/help/sale-share-opt-out"

            @Composable
            override fun Content() = WebScreen(this)
        }
    }

    data object LoginModal : NamedScreen {
        override val name = "Log in or sign up"
        override val key = uniqueScreenKey

        @Composable
        override fun Content() = RenderLogin()
    }
}