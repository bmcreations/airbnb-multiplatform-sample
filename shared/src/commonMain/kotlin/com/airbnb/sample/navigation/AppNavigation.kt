package com.airbnb.sample.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun AppNavigation() {
    Navigator(Screens.graph)
}