package com.airbnb.sample.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import cocoapods.GoogleMaps.GMSCameraUpdate
import cocoapods.GoogleMaps.GMSCameraUpdate.Companion.scrollByX
import cocoapods.GoogleMaps.GMSCameraUpdate.Companion.zoomIn
import cocoapods.GoogleMaps.GMSCameraUpdate.Companion.zoomOut
import cocoapods.GoogleMaps.GMSCoordinateBounds
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMapViewDelegateProtocol
import cocoapods.GoogleMaps.GMSMarker
import cocoapods.GoogleMaps.animateWithCameraUpdate
import cocoapods.GoogleMaps.kGMSMaxZoomLevel
import cocoapods.GoogleMaps.kGMSMinZoomLevel
import cocoapods.GoogleMaps.kGMSTypeNormal
import com.airbnb.sample.data.houses.Stay
import com.airbnb.sample.data.location.LatLong
import com.airbnb.sample.data.maps.CameraPositionBounds
import com.airbnb.sample.data.maps.MapSettings
import com.airbnb.sample.utils.formatAsMoney
import com.airbnb.sample.utils.uikit.UIPaddingLabel
import com.airbnb.sample.utils.uikit.addDropShadow
import com.airbnb.sample.utils.uikit.asImage
import com.airbnb.sample.utils.uikit.roundCorners
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import org.lighthousegames.logging.logging
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UIImage
import platform.UIKit.UITextAlignmentCenter
import platform.UIKit.systemGray5Color
import platform.darwin.NSObject
import kotlin.math.roundToInt

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MapView(
    modifier: Modifier,
    contentPadding: PaddingValues,
    mapSettings: MapSettings,
    userLocation: LatLong?,
    resultLocations: List<Stay.Minimal>,
    useTotalPrice: Boolean,
    onMarkerSelectionChange: (String?) -> Unit,
    onUpdateUserLocation: () -> Unit,
    onMapMoved: () -> Unit,
) {

    val googleMapView = remember { GMSMapView() }

    var isMapRedrawTriggered by remember { mutableStateOf(false) }
    var currentSelectedMarker by remember { mutableStateOf<GMSMarker?>(null) }
    var previousSelectedMarkers by remember {
        mutableStateOf(emptyList<String>())
    }

    val delegate = object : NSObject(), GMSMapViewDelegateProtocol {
        override fun mapView(
            mapView: GMSMapView,
            didTapAtCoordinate: CValue<CLLocationCoordinate2D>
        ) {
            currentSelectedMarker = null
            isMapRedrawTriggered = true
        }

        override fun mapView(mapView: GMSMapView, didTapMarker: GMSMarker): Boolean {
            currentSelectedMarker = didTapMarker
            isMapRedrawTriggered = true
            return true
        }
    }

    var isMapSetupCompleted by remember { mutableStateOf(false) }

    var didMapTypeChange by remember { mutableStateOf(false) }
    var didCameraPositionLatLongBoundsChange by remember { mutableStateOf(false) }
    var didCameraPositionChange by remember { mutableStateOf(false) }
    var didCameraLocationLatLongChange by remember { mutableStateOf(false) }

    val cameraPositionBounds by remember(resultLocations) {
        derivedStateOf {
            CameraPositionBounds(
                coordinates = resultLocations.map { it.location },
                padding = 80  // in pixels
            )
        }
    }

    LaunchedEffect(currentSelectedMarker) {
        val id = currentSelectedMarker?.userData() as? String
        if (id != null) {
            previousSelectedMarkers += id
        }
        onMarkerSelectionChange(id)
    }

    LaunchedEffect(resultLocations, useTotalPrice) {
        logging("map").d { "condition changed - redrawing" }
        isMapRedrawTriggered = true
    }

    LaunchedEffect(cameraPositionBounds) {
        didCameraPositionLatLongBoundsChange = true
        logging("map").d { "didCameraPositionLatLongBoundsChange changed" }
    }

    LaunchedEffect(userLocation) {
        if (userLocation != null) {
            didCameraLocationLatLongChange = true
            logging("map").d { "didCameraLocationLatLongChange changed" }
        }
    }

    val scope = rememberCoroutineScope()

    // Note: `GoogleMaps` using UIKit is a bit of a hack, it's not a real Composable, so we have to
    //       trigger independent updates of the map parts, and sometimes re-render the
    //       map elements. That's why theres all these `is` variables, and the `isRedrawMapTriggered`
    //       variable.
    //       If its not done like this, the UI for the map will not allow the user to move around.
    Box(Modifier.fillMaxSize()) {
        // Google Maps
        UIKitView(
            modifier = modifier.fillMaxSize(),
            interactive = true,
            factory = {
                googleMapView.delegate = delegate
                return@UIKitView googleMapView
            },
            update = { view ->
                // set the map up only once, this allows the user to move the map around
                if (!isMapSetupCompleted) {
                    view.settings.setAllGesturesEnabled(true)
                    view.settings.setScrollGestures(true)
                    view.settings.setZoomGestures(true)
                    view.settings.setCompassButton(false)

                    view.mapType = kGMSTypeNormal

                    view.myLocationEnabled = true // show the users dot
                    view.settings.myLocationButton = false // we use our own location circle

                    isMapSetupCompleted = true
                }

                if (didCameraLocationLatLongChange) {
                    didCameraLocationLatLongChange = false
                    userLocation?.let { cameraLocation ->
                        view.animateWithCameraUpdate(
                            GMSCameraUpdate.setTarget(
                                CLLocationCoordinate2DMake(
                                    latitude = cameraLocation.latitude,
                                    longitude = cameraLocation.longitude
                                )
                            )
                        )
                    }
                }

                if (didCameraPositionLatLongBoundsChange) {
                    didCameraPositionLatLongBoundsChange = false
                    cameraPositionBounds.let { cameraPositionLatLongBounds ->
                        var bounds = GMSCoordinateBounds()

                        cameraPositionLatLongBounds.coordinates.forEach { latLong ->
                            bounds = bounds.includingCoordinate(
                                CLLocationCoordinate2DMake(
                                    latitude = latLong.latitude,
                                    longitude = latLong.longitude
                                )
                            )
                        }
                        view.animateWithCameraUpdate(
                            GMSCameraUpdate.fitBounds(
                                bounds,
                                cameraPositionLatLongBounds.padding.toDouble()
                            )
                        )
                    }
                }

                if (isMapRedrawTriggered) {
                    // reset the markers & polylines, selected marker, etc.
                    val oldSelectedMarker = view.selectedMarker
                    val curSelectedMarkerId = currentSelectedMarker?.userData as? String
                    view.clear()

                    // render the markers
                    resultLocations.forEach { result ->
                        val tempMarker = GMSMarker().apply {
                            position = CLLocationCoordinate2DMake(
                                result.location.latitude,
                                result.location.longitude
                            )
                            userData = result.id
                            icon = pricedMarker(
                                price = if (useTotalPrice) "${result.totalPriceOfStay()}"
                                else "${result.usdPricePoint.roundToInt()}",
                                isSelected = curSelectedMarkerId == result.id,
                                wasPreviouslySelected = previousSelectedMarkers.contains(result.id)
                            )
                            map = view
                        }

                        if (tempMarker.userData as String == curSelectedMarkerId) {
                            currentSelectedMarker = tempMarker
                        }
                    }

                    // re-select the marker (if it was selected before)
                    oldSelectedMarker?.let { _ ->
                        view.selectedMarker = currentSelectedMarker
                    }

                    isMapRedrawTriggered = false
                }
            },
        )

        MapA11yControls(
            modifier = Modifier
                .padding(top = contentPadding.calculateTopPadding())
                .align(Alignment.TopEnd),
            mapSettings = mapSettings,
            canZoomOut = {
                logging("maps").d { "zoom=${googleMapView.camera.zoom}, minZoom = $kGMSMinZoomLevel" }
                googleMapView.camera.zoom > kGMSMinZoomLevel
            },
            canZoomIn = {
                logging("maps").d { "zoom=${googleMapView.camera.zoom}, maxZoom = $kGMSMaxZoomLevel" }
                googleMapView.camera.zoom < kGMSMaxZoomLevel
            },
            canMove = { direction -> true }
        ) {
            when (it) {
                MapControlEvent.Locate -> {
                    onUpdateUserLocation()
                }

                is MapControlEvent.Pan -> {
                    when (it.direction) {
                        MapControlEvent.MovementDirection.Left -> googleMapView.moveCamera(
                            scrollByX(
                                -50.0,
                                0.0
                            )
                        )

                        MapControlEvent.MovementDirection.Up -> googleMapView.moveCamera(
                            scrollByX(
                                0.0,
                                -50.0
                            )
                        )

                        MapControlEvent.MovementDirection.Right -> googleMapView.moveCamera(
                            scrollByX(50.0, 0.0)
                        )

                        MapControlEvent.MovementDirection.Down -> googleMapView.moveCamera(
                            scrollByX(
                                00.0,
                                50.0
                            )
                        )
                    }
                }

                MapControlEvent.ZoomIn -> {
                    googleMapView.moveCamera(zoomIn())
                }

                MapControlEvent.ZoomOut -> {
                    googleMapView.moveCamera(zoomOut())
                }
            }
        }
    }
}


@OptIn(ExperimentalForeignApi::class)
private fun pricedMarker(
    price: String,
    wasPreviouslySelected: Boolean,
    isSelected: Boolean
): UIImage {
    // contents
    val marker = UIPaddingLabel().apply {
        text = "\$${price.formatAsMoney()}"
        textAlignment = UITextAlignmentCenter
        font = UIFont.boldSystemFontOfSize(13.0)
        textColor = if (isSelected) UIColor.whiteColor else UIColor.blackColor
        paddingLeft = 8.0
        paddingRight = 8.0
        paddingTop = 8.0
        paddingBottom = 8.0
        sizeToFit()
    }


    marker.layer.backgroundColor = when {
        isSelected -> UIColor.blackColor
        wasPreviouslySelected -> UIColor.systemGray5Color
        else -> UIColor.whiteColor
    }.CGColor

    if (!isSelected) {
        marker.layer.borderColor = UIColor.lightGrayColor.CGColor
        marker.layer.borderWidth = 1.0
    }
    marker.clipsToBounds = false
    marker.roundCorners(marker.frame.useContents { size.height / 2 })
    marker.addDropShadow(offsetY = 3.0)

    return marker.asImage()
}

