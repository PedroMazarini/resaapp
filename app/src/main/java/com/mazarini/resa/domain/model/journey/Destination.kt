package com.mazarini.resa.domain.model.journey

import kotlinx.serialization.Serializable

@Serializable
data class Destination(
    val name: String,
    val platform: Platform? = null,
    val arrivalTime: JourneyTimes,
)
