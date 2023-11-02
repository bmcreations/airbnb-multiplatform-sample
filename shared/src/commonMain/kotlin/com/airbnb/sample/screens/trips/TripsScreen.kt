package com.airbnb.sample.screens.trips

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.sample.navigation.LocalPlatformNavigator
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.ui.components.UserGateKeeper

@Composable
internal fun Screens.Main.Trips.RenderTrips() {
    Content(modifier = Modifier.fillMaxSize())
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {
    val navigator = LocalPlatformNavigator.current

    // TODO: support authenticated state
    UserGateKeeper(
        modifier,
        title = "Trips",
        messageHeading = "No trips yet",
        messageBody = "When you\'re ready to plan your next trip, we\'re here to help.",
    ) {
        navigator.show(Screens.LoginModal)
    }
}