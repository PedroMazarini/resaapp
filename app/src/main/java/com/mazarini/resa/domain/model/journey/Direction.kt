package com.mazarini.resa.domain.model.journey

import com.mazarini.resa.domain.model.Coordinate
import kotlinx.serialization.Serializable

@Serializable
data class Direction(
    val from: Coordinate?,
    val to: Coordinate?,
)