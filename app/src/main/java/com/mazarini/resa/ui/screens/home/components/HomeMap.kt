package com.mazarini.resa.ui.screens.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.ui.screens.home.state.HomeUiState
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews
import com.mazarini.resa.ui.util.mapsconfiguration.MapsZoomType
import com.mazarini.resa.ui.util.mapsconfiguration.StaticMaps

@Composable
fun HomeMap(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
) {
    val hasLocationAccess by remember { uiState.hasLocationPermission }
    val currentLocation by remember { uiState.currentLocation }

    val zoomType = getZoomType(hasLocationAccess, currentLocation)
    StaticMaps(
        modifier = modifier,
        mapsZoomType = zoomType
    )
}

fun getZoomType(hasLocationAccess: Boolean, currentLocation: Coordinate?): MapsZoomType {
    return if (hasLocationAccess && currentLocation != null) {
        MapsZoomType.MyLocation(lat = currentLocation.lat, lon = currentLocation.lon)
    } else {
        MapsZoomType.Default
    }
}

@Composable
@Previews
fun HomeMapPreview() {
    ResaTheme {
        HomeMap(
            uiState = HomeUiState()
        )
    }
}
