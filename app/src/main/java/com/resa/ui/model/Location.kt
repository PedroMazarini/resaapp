package com.resa.ui.model

import com.resa.R

data class Location(
    val id: String,
    val name: String,
    val lat: Double? = null,
    val lon: Double? = null,
    val type: LocationType,
)

enum class LocationType {
    unknown,
    stoparea,
    stoppoint,
    address,
    pointofinterest,
    metastation,
    gps;

    companion object {
        infix fun fromString(value: String): LocationType {
            return LocationType.valueOf(value)
        }
    }

    fun iconResource(): Int =
        when (this) {
            address -> R.drawable.ic_road
            stoparea, stoppoint -> R.drawable.ic_bus_stop
            else -> R.drawable.ic_building
        }
}
