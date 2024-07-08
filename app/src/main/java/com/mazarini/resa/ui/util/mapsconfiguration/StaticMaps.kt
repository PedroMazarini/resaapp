package com.mazarini.resa.ui.util.mapsconfiguration

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.mazarini.resa.ui.theme.MTheme
import kotlinx.coroutines.launch

@Composable
fun StaticMaps(
    modifier: Modifier = Modifier,
    mapsZoomType: MapsZoomType = MapsZoomType.Default,
) {

    val coroutineScope = rememberCoroutineScope()
    val currentZoomType by remember { mutableStateOf(mapsZoomType) }
    val mapStyling = MTheme.mapsStyling.json

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                zoomControlsEnabled = false,
                zoomGesturesEnabled = false,
                scrollGesturesEnabled = false,
                myLocationButtonEnabled = false,
            ),
        )
    }
    val mapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions(mapStyling),
        isMyLocationEnabled = mapsZoomType is MapsZoomType.MyLocation,
    )

    val cameraZoomTo = getCameraZoom(mapsZoomType)
    val zoomLevel = getZoomLevel(mapsZoomType)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cameraZoomTo, zoomLevel.level)
    }

    GoogleMap(
        modifier = modifier,
        properties = mapProperties,
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings,
        contentPadding = PaddingValues(start = 24.dp, bottom = 29.dp)
    ){

        SideEffect {
            coroutineScope.launch {
                cameraPositionState.animate(CameraUpdateFactory.newLatLng(cameraZoomTo))
                cameraPositionState.animate(CameraUpdateFactory.zoomTo(zoomLevel.level))
            }
        }
    }

    LaunchedEffect(currentZoomType) {
        if (currentZoomType is MapsZoomType.MyLocation) {
            coroutineScope.launch {
                cameraPositionState.animate(CameraUpdateFactory.newLatLng(cameraZoomTo))
                cameraPositionState.animate(CameraUpdateFactory.zoomTo(zoomLevel.level))
            }
        }
    }

}

fun getCameraZoom(mapsZoomType: MapsZoomType): LatLng {
    return when (mapsZoomType) {
        MapsZoomType.Default -> LatLng(57.679932, 12.014784) // Gothenburg
        is MapsZoomType.MyLocation -> LatLng(57.679932, 12.014784)
    }
}

fun getZoomLevel(mapsZoomType: MapsZoomType): ZoomLevels {
    return when (mapsZoomType) {
        MapsZoomType.Default -> ZoomLevels.City
        is MapsZoomType.MyLocation -> ZoomLevels.Streets
    }
}