package com.mazarini.resa.domain.model

data class Location(
    val id: String,
    val name: String,
    val lat: Double?,
    val lon: Double?,
    val type: LocationType,
) {
    fun latLng(): String {
        return "$lat$lon"
    }
}
