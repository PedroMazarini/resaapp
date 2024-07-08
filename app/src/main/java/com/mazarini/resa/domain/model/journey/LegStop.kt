package com.mazarini.resa.domain.model.journey

import com.mazarini.resa.domain.model.Coordinate
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
data class LegStop(
    val id: String,
    val name: String,
    val time: String,
    val isPartOfLeg: Boolean,
    val isLegStart: Boolean,
    val isLegEnd: Boolean,
    val coordinate: Coordinate?,
)