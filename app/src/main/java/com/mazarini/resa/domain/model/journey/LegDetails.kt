package com.mazarini.resa.domain.model.journey

import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.global.extensions.tryCatch
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
data class LegDetails(
    val index: Int,/** repeats here to match when joining with Leg */
    val isLastLeg: Boolean,
    val platForm: Platform? = null,
    val destination: Destination? = null,
    val legStops: List<LegStop> = emptyList(),
    val pathWay: List<Coordinate> = emptyList(),
)

val LegDetails.departName: String
    get() = tryCatch { legStops.first{ it.isLegStart }.name } ?: ""
