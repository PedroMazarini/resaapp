package com.resa.ui.util

import com.resa.R
import com.resa.domain.model.TransportMode

fun TransportMode.iconResource(): Int {
    return when (this) {
        TransportMode.tram -> R.drawable.ic_tram
        TransportMode.bus -> R.drawable.ic_bus
        TransportMode.ferry -> R.drawable.ic_boat
        TransportMode.train -> R.drawable.ic_train
        TransportMode.taxi -> R.drawable.ic_taxi
        TransportMode.walk -> R.drawable.ic_walk
        TransportMode.bike -> R.drawable.ic_bike
        TransportMode.car -> R.drawable.ic_car
        else -> R.drawable.ic_bus
    }
}
