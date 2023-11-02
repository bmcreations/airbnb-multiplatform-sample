package com.airbnb.sample.screens.explore

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.sample.navigation.Screens

@Composable
internal fun Screens.Main.Explore.RenderExplore() {
    Content(modifier = Modifier.fillMaxSize())
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {
    Text("Explore")
}