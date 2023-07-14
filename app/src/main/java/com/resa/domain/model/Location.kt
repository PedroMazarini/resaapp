package com.resa.domain.model

data class Location(
    val id: String,
    val name: String,
    val lat: Double?,
    val lon: Double?,
    val type: LocationType,
)
