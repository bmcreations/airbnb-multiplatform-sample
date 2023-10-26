package com.airbnb.sample.screens.wishlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.ui.components.UserGateKeeper

@Composable
internal fun Screens.Wishlists.Render() {
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
        title = "Wishlists",
        messageHeading = "Log in to view your wishlists",
        messageBody = "You can create, view, or edit wishlists once you\'ve logged in.",
    ) {
        navigator.show(Screens.LoginModal)
    }
}