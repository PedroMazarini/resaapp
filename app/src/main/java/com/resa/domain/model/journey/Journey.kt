package com.resa.domain.model.journey

data class Journey(
    val id: String,
    val departure: Departure,
    val durationInMinutes: Int,
    val hasAccessibility: Boolean,
    val hasAlert: Boolean,
    val arrivalTimes: JourneyTimes,
    val isDeparted: Boolean,
    val legs: List<Leg>,
)