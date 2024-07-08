package com.mazarini.resa.ui.screens.journeydetails.components.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.iconResource

@Composable
fun TrackingMarker(
    bus: VehiclePosition,
    markerState: MarkerState,
) {
    MarkerComposable(
        title = bus.directionName,
        keys = arrayOf("id"),
        state = markerState,
        onClick = { false }, // TODO: Handle click if needed or remove,
    ) {
        TrackingMarkerContent(bus = bus)
    }
}

@Composable
fun TrackingMarkerContent(bus: VehiclePosition) {
    Box(
        modifier = Modifier
            .padding(1.dp)
            .clip(CircleShape)
            .background(bus.colors.primaryColor())
    ) {
        Icon(
            modifier = Modifier
                .padding(4.dp)
                .size(24.dp),
            painter = painterResource(id = bus.transportMode.iconResource()),
            contentDescription = null,
            tint = bus.colors.secondaryColor(),
        )
    }
}

@Composable
@Preview
fun TrackingMarkerPreview() {
    ResaTheme {
        TrackingMarkerContent(
            bus = FakeFactory.busTracking(),
        )
    }
}