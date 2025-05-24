package com.mazarini.resa.ui.screens.journeydetails.components.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.journey.LegColors
import com.mazarini.resa.domain.model.journey.LegDetails
import com.mazarini.resa.global.extensions.isBounds
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun MakeLegStops(
    details: LegDetails,
    colors: LegColors,
    isLastLeg: Boolean,
) {
    details.legStops.forEachIndexed { index, stop ->
        stop.coordinate?.let { coordinate ->
            if (details.legStops isBounds index) {

                MarkerComposable(
                    title = stop.name,
                    keys = arrayOf(stop.id),
                    state = MarkerState(position = coordinate.toLatLng()),
                ) {
                    if (!isLastLeg || index == 0) {
                        StopBoundsMarker(colors)
                    } else {
                        DestinationTransportMarker()
                    }
                }
            } else {
                MarkerComposable(
                    title = stop.name,
                    keys = arrayOf(stop.id),
                    state = MarkerState(position = coordinate.toLatLng()),
                ) {
                    StopMarker(colors)
                }
            }
        }
    }
}

@Composable
fun DestinationTransportMarker() {
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

@Composable
fun StopBoundsMarker(colors: LegColors) {
    Box {
        Image(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.ic_pin_filled),
            colorFilter = ColorFilter.tint(colors.primaryColor(), BlendMode.Modulate),
            contentDescription = null,
        )
    }
}

@Composable
fun StopMarker(colors: LegColors) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .border(2.dp, colors.primaryColor(), CircleShape)
            .padding(1.dp)
            .clip(CircleShape)
            .background(colors.secondaryColor())
    )
}

@Composable
@Preview
fun StopBoundsMarkerPreview() {
    ResaTheme {
        StopBoundsMarker(
            colors = LegColors(
                foreground = Color.White,
                background = Color.Red,
                border = Color.Blue,
            ),
        )
    }
}

@Composable
@Preview
fun MakeLegStopsPreview() {
    ResaTheme {
        StopMarker(
            colors = LegColors(
                foreground = Color.White,
                background = Color.Red,
                border = Color.Red,
            ),
        )

    }
}