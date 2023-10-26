package com.airbnb.sample.screens.trips

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.ui.components.UserGateKeeper

@Composable
internal fun Screens.Trips.Render() {
    Content(modifier = Modifier.fillMaxSize())
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {
    val navigator = LocalBottomSheetNavigator.current

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