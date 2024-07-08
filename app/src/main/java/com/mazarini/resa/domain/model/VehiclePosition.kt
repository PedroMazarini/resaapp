package com.mazarini.resa.domain.model

import com.mazarini.resa.domain.model.journey.LegColors

data class VehiclePosition(
    val name: String,
    val directionName: String,
    val colors: LegColors,
    val position: Coordinate,
    val transportMode: TransportMode,
)

val VehiclePosition.id: Int
    get() = (name+directionName).hashCode()
