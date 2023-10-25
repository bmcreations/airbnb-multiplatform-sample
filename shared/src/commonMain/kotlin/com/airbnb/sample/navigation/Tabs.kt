package com.airbnb.sample.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.airbnb.sample.screens.profile.Render
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

internal sealed interface Tabs {
    sealed interface Anonymous : Tabs {
        data object Explore : Tabs, Tab {
            @Composable
            override fun Content() {

            }

            override val options: TabOptions
                @Composable get() {
                    val icon = rememberVectorPainter(Icons.Rounded.Search)
                    return remember {
                        TabOptions(
                            index = 0u,
                            title = "Explore",
                            icon = icon
                        )
                    }
                }
        }

        data object Wishlists : Tabs, Tab {
            @Composable
            override fun Content() {

            }

            override val options: TabOptions
                @Composable get() = TabOptions(
                    index = 1u,
                    title = "Wishlists",
                    icon = rememberVectorPainter(Icons.Rounded.FavoriteBorder)
                )
        }

        data object Trips : Tabs, Tab {
            @Composable
            override fun Content() {

            }

            @OptIn(ExperimentalResourceApi::class)
            override val options: TabOptions
                @Composable get() = TabOptions(
                    index = 2u,
                    title = "Trips",
                    icon = painterResource("drawable/ic_airbnb.xml")
                )
        }

        data object Inbox : Tabs, Tab {
            @Composable
            override fun Content() {

            }

            @OptIn(ExperimentalResourceApi::class)
            override val options: TabOptions
                @Composable get() = TabOptions(
                    index = 3u,
                    title = "Inbox",
                    icon = painterResource("drawable/ic_chat_centered.xml")
                )
        }

        data object LogIn : Tabs, Tab {
            @Composable
            override fun Content() {
                Screens.Profile.Render()
            }

            override val options: TabOptions
                @Composable get() = TabOptions(
                    index = 4u,
                    title = "Log in",
                    icon = rememberVectorPainter(Icons.Rounded.AccountCircle)
                )
        }
    }
}