package com.resa.domain.model.journey

data class Journey(
    val id: String,
    val departure: Departure,
    val durationInMinutes: Int,
    val hasAccessibility: Boolean,
    val warning: WarningTypes,
    val arrivalTimes: JourneyTimes,
    val isDeparted: Boolean,
    val occupancyLevel: OccupancyLevel,
    val legs: List<Leg>,
)