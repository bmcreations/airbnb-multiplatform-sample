package dev.bmcreations.template

import androidx.compose.runtime.Composable
import dev.bmcreations.template.navigation.AppNavigation
import dev.bmcreations.template.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        AppNavigation()
    }
}

expect fun getPlatformName(): String