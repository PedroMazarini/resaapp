package com.mazarini.resa.ui.screens.journeydetails.components.map

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.id
import com.mazarini.resa.domain.model.journey.Leg
import com.mazarini.resa.domain.model.journey.LegType
import com.mazarini.resa.domain.model.samePosition
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiState
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.fontSize
import com.mazarini.resa.ui.util.mapsconfiguration.ZoomLevels
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailsMap(
    modifier: Modifier,
    uiState: JourneyDetailsUiState,
    onEvent: (JourneyDetailsUiEvent) -> Unit,
    mapLoadedInit: Boolean = false,
) {
    val hasLocationAccess by uiState.hasLocationAccess
    val selectedJourney by uiState.selectedJourney.collectAsState()
    val followingVehicle by uiState.followingVehicle
    val mapLoaded = remember { mutableStateOf(mapLoadedInit) }
    val legs = selectedJourney?.legs.orEmpty()
    val coroutineScope = rememberCoroutineScope()
    val mapStyling = MTheme.mapsStyling.json
    val uiSettings = getUiSettings()
    val busesTracking = uiState.trackedVehicles.value
    val trackingStates: Map<String, MarkerState> = busesTracking.associate {
        it.name to rememberMarkerState()
    }
    val mapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions(mapStyling),
        isMyLocationEnabled = hasLocationAccess,
    )

    val zoomLevel = ZoomLevels.Streets
    val cameraPositionState = rememberCameraPositionState()
    val screenBorder = getScreenBorder(followingVehicle)

    Box(modifier = modifier
        .fillMaxSize()
        .border(screenBorder)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
            contentPadding = PaddingValues(start = 24.dp, bottom = 8.dp),
            onMapLoaded = { mapLoaded.value = true },
        ) {
            val bounds = getLegsPolylines(legs)


            busesTracking.forEach { bus ->
                trackingStates[bus.name]?.let { markerState ->
                    TrackingMarker(bus = bus, markerState = markerState)
                }
            }
            LaunchedEffect(Unit) {
                coroutineScope.launch {

                    cameraPositionState.animate(CameraUpdateFactory.zoomTo(zoomLevel.level))
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngBounds(
                            getZoomBounds(
                                bounds
                            ), 10
                        )
                    )
                }
            }
            LaunchedEffect(key1 = followingVehicle?.position) {
                coroutineScope.launch {
                    with(followingVehicle ?: return@launch) {
                        val cameraPosition = CameraPosition.fromLatLngZoom(this.position.toLatLng(), ZoomLevels.Blocks.level)
                        cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }
                }
            }
            LaunchedEffect(key1 = cameraPositionState.cameraMoveStartedReason) {
                if (cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE &&
                    followingVehicle != null) {
                    onEvent(JourneyDetailsUiEvent.StopFollowingVehicle)
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 24.dp, top = 12.dp),
        ) {
            CloseButton {
                onEvent(JourneyDetailsUiEvent.SetShouldShowMap(false))
            }

            FollowButtons(uiState, onEvent)
        }

        if (!mapLoaded.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MTheme.colors.background)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(.4f)
                        .blur(4.dp),
                    painter = painterResource(id = MTheme.res.mapBg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.loading_journey),
                    style = MTheme.type.textField.fontSize(24.sp),
                )
            }
        }
    }

    BackHandler {
        onEvent(JourneyDetailsUiEvent.SetShouldShowMap(false))
    }
    val movingTransitions = remember { mutableStateOf(0) }

    if (movingTransitions.value == 0) {
        busesTracking.forEach { bus ->
            trackingStates[bus.name]?.let { markerState ->
                if (!markerState.position.samePosition(bus.position)) {
                    movingTransitions.value++
                    coroutineScope.launch {
                        animateMarker(
                            posTarget = bus.position.toLatLng(),
                            markerPos = markerState.position,
                            onUpdate = {
                                markerState.position = it
                            },
                            onFinished = {
                                movingTransitions.value--
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getScreenBorder(followingVehicle: VehiclePosition?): BorderStroke {
    followingVehicle?.let {
        return BorderStroke(2.dp, it.colors.primaryColor())
    } ?: run {
        return BorderStroke(0.dp, Color.Transparent)
    }
}

@Composable
fun FollowButtons(
    uiState: JourneyDetailsUiState,
    onEvent: (JourneyDetailsUiEvent) -> Unit,
) {
    val trackedVehicles by uiState.trackedVehicles
    val followingVehicle by uiState.followingVehicle
    trackedVehicles.forEach { vehicle ->
        FollowButton(
            modifier = Modifier.padding(top = 8.dp),
            vehicle = vehicle,
            isFollowing = vehicle.id == followingVehicle?.id,
            onEvent = onEvent,
        )
    }
}

private suspend fun animateMarker(
    posTarget: LatLng,
    markerPos: LatLng,
    onUpdate: (LatLng) -> Unit,
    onFinished: () -> Unit,
) {
    var latSum = markerPos.latitude
    var lonSum = markerPos.longitude

    if (markerPos.notInitialized()) {
        onUpdate(posTarget)
        onFinished()
        return
    }
    for (i in 0..9) {
        delay(500)
        latSum += (posTarget.latitude - markerPos.latitude) * 0.1f
        lonSum += (posTarget.longitude - markerPos.longitude) * 0.1f
        onUpdate(LatLng(latSum, lonSum))
    }
    onUpdate(posTarget)
    onFinished()
}

private fun LatLng.notInitialized(): Boolean {
    return latitude == 0.0 && longitude == 0.0
}

@Composable
fun ColumnScope.CloseButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = { onClick() },
        modifier = Modifier
            .align(Alignment.End)
            .clip(CircleShape)
            .background(MTheme.colors.background)
            .size(32.dp)
    ) {
        Icon(
            painterResource(id = R.drawable.ic_close),
            contentDescription = null,
            tint = MTheme.colors.textPrimary,
        )
    }
}

private fun getZoomBounds(bounds: List<LatLng>): LatLngBounds {
    val boundsBuilder = LatLngBounds.builder()
    bounds.forEach { boundsBuilder.include(it) }
    if (bounds.isEmpty()) {
        boundsBuilder.include(LatLng(57.679932, 12.014784)) // Gothenburg
    }
    return boundsBuilder.build()
}

@Composable
fun getLegsPolylines(legs: List<Leg>): List<LatLng> {
    val boundsToInclude = mutableListOf<LatLng>()
    legs.forEachIndexed { index, leg ->
        leg.details?.let { details ->
            if (leg.legType == LegType.TRANSPORT) {
                val legBounds = transportPolyline(pathWay = details.pathWay, colors = leg.colors)
                MakeLegStops(details, leg.colors, details.isLastLeg)
                boundsToInclude.addAll(legBounds)
            } else {
                val points = listOfNotNull(
                    leg.direction?.from?.toLatLng(),
                    leg.direction?.to?.toLatLng()
                )
                Polyline(
                    points = points,
                    color = MTheme.colors.primary,
                    pattern = listOf(Dot(), Gap(6f))
                )
                boundsToInclude.addAll(points)
                if (leg.legType == LegType.ARRIVAL_LINK && points.isNotEmpty()) {
                    DestinationWalkMarker(index, details.destination?.name.orEmpty(), points.last())
                }
            }
        }
    }
    return boundsToInclude
}

@Composable
fun getUiSettings(): MapUiSettings {
    val settings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true,
                scrollGesturesEnabled = true,
                myLocationButtonEnabled = false,
            ),
        )
    }
    return settings
}

@Composable
fun DestinationWalkMarker(
    id: Int,
    name: String,
    position: LatLng,
) {
    MarkerComposable(
        title = name,
        keys = arrayOf(id),
        state = MarkerState(position = position),
        onClick = { false }, // TODO: Handle click if needed or remove,
    ) {
        Box {
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_destination),
                contentDescription = null,
            )
        }
    }
}

@Composable
@Preview
fun DetailsMapPreview() {
    ResaTheme(darkTheme = false) {
        DetailsMap(
            modifier = Modifier.background(color = Color.White),
            uiState = JourneyDetailsUiState(),
            onEvent = {},
            mapLoadedInit = true,
        )
    }
}