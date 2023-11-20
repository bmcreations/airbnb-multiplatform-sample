package com.airbnb.sample.screens.explore.experience.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.ArrowLeft
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.ZoomInMap
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.airbnb.sample.data.maps.MapSettings
import com.airbnb.sample.inject.LocalAppComponent
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.MapView
import kotlinx.coroutines.launch

@Composable
internal fun MapContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    mapSettings: MapSettings,
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

    val transitionState = remember {
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
            contentPadding = contentPadding,
            mapSettings = mapSettings,
            userLocation = userLocation,
            resultLocations = results,
            useTotalPrice = useTotalPrice,
            onMarkerSelectionChange = { id -> displayListingId = id },
            onUpdateUserLocation = {
                scope.launch {
                    userLocation = locationProvider.getCurrentLocation()
                        .let { LatLong(it.latitude, it.longitude) }
                }
            },
            onMapMoved = {

            }
        )

        // listing decor
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
        }
    }
}

