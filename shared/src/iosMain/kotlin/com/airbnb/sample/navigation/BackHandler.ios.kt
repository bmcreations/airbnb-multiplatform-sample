package com.airbnb.sample.navigation

import androidx.compose.runtime.Composable

// TODO: use ios backstack
@Composable
internal actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) = Unit