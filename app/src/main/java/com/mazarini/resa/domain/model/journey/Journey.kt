package com.mazarini.resa.domain.model.journey

import kotlinx.serialization.Serializable

@Serializable
data class Journey(
    val id: String,
    val detailsId: String,
    val departure: Departure,
    val durationInMinutes: Int,
    val transportDurationInMinutes: Int,
    val hasAccessibility: Boolean,
    val warning: WarningTypes,
    val arrivalTimes: JourneyTimes,
    val isDeparted: Boolean,
    val occupancyLevel: OccupancyLevel,
    val originName: String? = null,
    val destName: String? = null,
    val legs: List<Leg>,
)