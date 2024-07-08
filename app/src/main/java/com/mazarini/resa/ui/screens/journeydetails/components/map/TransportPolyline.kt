package com.mazarini.resa.ui.screens.journeydetails.components.map

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.CustomCap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.journey.LegColors
import com.mazarini.resa.domain.model.splitTripStops
import com.mazarini.resa.global.extensions.tryCatch
import com.mazarini.resa.ui.util.bitmapDescriptorFromRes

@Composable
fun transportPolyline(
    pathWay: List<Coordinate>,
    colors: LegColors,
): List<LatLng> {
    val cap = bitmapDescriptorFromRes(resId = R.drawable.ic_point_circle_outline)
    val (prior, trip, post) = pathWay.splitTripStops()

    Polyline(
        points = prior,
        color = colors.primaryColor().copy(alpha = 0.5f),
        startCap = CustomCap(cap),
        endCap = CustomCap(cap),
    )
    Polyline(
        points = trip,
        color = colors.primaryColor(),
        startCap = CustomCap(cap),
        endCap = CustomCap(cap),
    )
    Polyline(
        points = post,
        color = colors.primaryColor().copy(alpha = 0.5f),
        startCap = CustomCap(cap),
        endCap = CustomCap(cap),
    )
    if (colors.hasBorder()) {
        Polyline(
            points = prior,
            color = colors.secondaryColor().copy(alpha = 0.5f),
            startCap = CustomCap(cap),
            endCap = CustomCap(cap),
            width = 8f,
        )
        Polyline(
            points = trip,
            color = colors.secondaryColor(),
            startCap = CustomCap(cap),
            endCap = CustomCap(cap),
            width = 8f,
        )
        Polyline(
            points = post,
            color = colors.secondaryColor().copy(alpha = 0.5f),
            startCap = CustomCap(cap),
            endCap = CustomCap(cap),
            width = 8f,
        )
    }
    return tryCatch {
        listOf(trip.first(), trip.last())
    } ?: emptyList()
}