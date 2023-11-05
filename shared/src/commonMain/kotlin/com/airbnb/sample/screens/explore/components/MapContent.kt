package com.airbnb.sample.screens.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airbnb.sample.data.houses.Stay

@Composable
internal fun MapContent(results: List<Stay.Minimal>, viewListing: (Stay.Minimal) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondaryContainer))
}