package com.airbnb.sample.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.ArrowLeft
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.airbnb.sample.data.houses.Stay
import com.airbnb.sample.data.location.LatLong
import com.airbnb.sample.data.maps.CameraPositionBounds
import com.airbnb.sample.data.maps.MapSettings
import com.airbnb.sample.screens.explore.ExploreViewModel
import com.airbnb.sample.services.location.LocationProvider
import com.airbnb.sample.theme.dimens
import com.airbnb.sample.ui.components.MapControlEvent.MovementDirection
import com.airbnb.sample.utils.ui.addIf
import com.airbnb.sample.utils.ui.itemsWithDividers

@Composable
expect fun MapView(
    modifier: Modifier,
    contentPadding: PaddingValues,
    mapSettings: MapSettings,
    userLocation: LatLong?,
    resultLocations: List<Stay.Minimal>,
    useTotalPrice: Boolean,
    onMarkerSelectionChange: (String?) -> Unit,
    onUpdateUserLocation: () -> Unit,
    onMapMoved: () -> Unit,
)


sealed interface MapControlEvent {
    val imageVector: ImageVector
    val contentDescription: String

    data object Locate : MapControlEvent {
        override val imageVector: ImageVector = Icons.Rounded.Navigation
        override val contentDescription: String = "Locate user"
    }

    enum class MovementDirection {
        Left, Up, Right, Down
    }

    data class Pan(val direction: MovementDirection) : MapControlEvent {
        override val imageVector: ImageVector = when (direction) {
            MovementDirection.Left -> Icons.Rounded.ArrowLeft
            MovementDirection.Up -> Icons.Rounded.ArrowDropUp
            MovementDirection.Right -> Icons.Rounded.ArrowRight
            MovementDirection.Down -> Icons.Rounded.ArrowDropDown
        }
        override val contentDescription: String = "Move ${direction.name}"
    }

    data object ZoomIn : MapControlEvent {
        override val imageVector: ImageVector = Icons.Rounded.Add
        override val contentDescription: String = "Zoom In"
    }

    data object ZoomOut : MapControlEvent {
        override val imageVector: ImageVector = Icons.Rounded.Remove
        override val contentDescription: String = "Zoom Out"
    }
}

@Composable
fun MapA11yControls(
    modifier: Modifier = Modifier,
    mapSettings: MapSettings,
    canZoomIn: () -> Boolean,
    canZoomOut: () -> Boolean,
    canMove: (MovementDirection) -> Boolean,
    onClick: (event: MapControlEvent) -> Unit,
) {
    val hasA11yControlsEnabled by remember(mapSettings) {
        derivedStateOf { mapSettings.panControls || mapSettings.zoomControls }
    }

    Card(
        modifier = modifier
            .addIf(hasA11yControlsEnabled) { Modifier.padding(top = MaterialTheme.dimens.inset) }
            .addIf(!hasA11yControlsEnabled) { Modifier.padding(MaterialTheme.dimens.inset) }
            .width(IntrinsicSize.Min),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.medium.let {
            if (hasA11yControlsEnabled) {
                it.copy(
                    topEnd = ZeroCornerSize,
                    bottomEnd = ZeroCornerSize
                )
            } else {
                it
            }
        },
    ) {
        val controls by remember(mapSettings) {
            derivedStateOf {
                val controls = mutableListOf<MapControlEvent>()
                controls.add(MapControlEvent.Locate)
                if (mapSettings.zoomControls) {
                    controls.addAll(listOf(MapControlEvent.ZoomIn, MapControlEvent.ZoomOut))
                }

                if (mapSettings.panControls) {
                    controls.addAll(
                        listOf(
                            MapControlEvent.Pan(MovementDirection.Up),
                            MapControlEvent.Pan(MovementDirection.Down),
                            MapControlEvent.Pan(MovementDirection.Left),
                            MapControlEvent.Pan(MovementDirection.Right)
                        )
                    )
                }

                controls.toList()
            }
        }
        itemsWithDividers(controls) { event ->
            when (event) {
                MapControlEvent.Locate -> {
                    MapControlButton(
                        imageVector = event.imageVector,
                        contentDescription = event.contentDescription
                    ) {
                        onClick(event)
                    }
                }

                is MapControlEvent.Pan -> {
                    MapControlButton(
                        enabled = canMove(event.direction),
                        imageVector = event.imageVector,
                        contentDescription = event.contentDescription
                    ) {
                        onClick(event)
                    }
                }

                MapControlEvent.ZoomIn -> {
                    MapControlButton(
                        enabled = canZoomIn(),
                        imageVector = event.imageVector,
                        contentDescription = event.contentDescription
                    ) {
                        onClick(event)
                    }
                }

                MapControlEvent.ZoomOut -> {
                    MapControlButton(
                        enabled = canZoomOut(),
                        imageVector = event.imageVector,
                        contentDescription = event.contentDescription
                    ) {
                        onClick(event)
                    }
                }
            }

        }
    }
}

@Composable
private fun MapControlButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(modifier = modifier
        .addIf(!enabled) { Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = ContentAlpha.disabled)) }
        .clickable(enabled = enabled) { onClick() }
    ) {
        Icon(
            modifier = Modifier
                .padding(MaterialTheme.dimens.staticGrid.x4),
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
    }
}