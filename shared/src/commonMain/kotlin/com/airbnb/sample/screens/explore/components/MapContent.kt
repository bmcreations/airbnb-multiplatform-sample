package com.airbnb.sample.screens.explore.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.sample.data.houses.Stay
import com.airbnb.sample.data.location.LatLong
import com.airbnb.sample.inject.LocalAppComponent
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.MapView
import kotlinx.coroutines.launch

@Composable
internal fun MapContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    results: List<Stay.Minimal>,
    useTotalPrice: Boolean,
    viewListing: (Stay.Minimal) -> Unit
) {
    val locationProvider = LocalAppComponent.current.locationProvider
    val scope = rememberCoroutineScope()
    var userLocation by remember { mutableStateOf<LatLong?>(null) }
    LaunchedEffect(Unit) {
        userLocation =
            locationProvider.getLatestLocation()?.let { LatLong(it.latitude, it.longitude) }
    }

    val transitionState = rememberSaveable {
        MutableTransitionState(false)
    }
    var displayListingId by rememberSaveable { mutableStateOf<String?>(null) }


    LaunchedEffect(displayListingId) {
        if (!transitionState.isIdle) return@LaunchedEffect
        transitionState.targetState = displayListingId != null
    }

    Box(modifier = modifier) {
        MapView(
            modifier = Modifier.matchParentSize(),
            userLocation = userLocation,
            resultLocations = results,
            useTotalPrice = useTotalPrice,
            onMarkerSelectionChange = { id -> displayListingId = id },
            onMapMoved = {

            }
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(contentPadding)
        ) {
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        horizontal = MaterialTheme.dimens.inset,
                        vertical = MaterialTheme.dimens.staticGrid.x3
                    ),
                visibleState = transitionState,
                enter = fadeIn(tween(500)),
                exit = fadeOut(tween(500))
            ) {
                val listing = results.find { it.id == displayListingId }
                ListingCard(listing = listing, useTotalPrice = useTotalPrice) {
                    listing?.let { viewListing(it) }
                }
            }

            LocationFinder(
                modifier = Modifier.align(Alignment.TopEnd)
                    .padding(MaterialTheme.dimens.inset)
            ) {
                scope.launch {
                    userLocation = locationProvider.getCurrentLocation()
                        .let { LatLong(it.latitude, it.longitude) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationFinder(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.padding(MaterialTheme.dimens.staticGrid.x2),
            imageVector = Icons.Rounded.Navigation,
            contentDescription = "Locate user"
        )
    }
}

