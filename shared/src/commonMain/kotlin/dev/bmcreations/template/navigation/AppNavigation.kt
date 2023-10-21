package dev.bmcreations.template.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import dev.bmcreations.template.screens.HomeScreen

@Composable
fun AppNavigation() {
    Navigator(HomeScreen)
}