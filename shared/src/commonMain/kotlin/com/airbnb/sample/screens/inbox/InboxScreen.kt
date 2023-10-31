package com.airbnb.sample.screens.inbox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.sample.navigation.LocalPlatformNavigator
import com.airbnb.sample.navigation.Screens
import com.airbnb.sample.ui.components.UserGateKeeper

@Composable
internal fun Screens.Inbox.Render() {
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
        title = "Inbox",
        messageHeading = "Log in to see messages",
        messageBody = "Once you login, you\'ll find messages from hosts here.",
    ) {
        navigator.show(Screens.LoginModal)
    }
}