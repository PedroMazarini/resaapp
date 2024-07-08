package com.mazarini.resa.ui.model

import com.mazarini.resa.R
import com.mazarini.resa.domain.model.LocationType as DomainLocationType

data class Location(
    val id: String,
    val name: String,
    val lat: Double? = null,
    val lon: Double? = null,
    val type: LocationType,
) {
    fun isGps(): Boolean = type == LocationType.gps
    fun customId(): String = if (isGps()) "gps" else "$lat$lon"
}

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

    fun asJourneyParamsType(): DomainLocationType {
        return when (this) {
            stoparea -> DomainLocationType.stoparea
            stoppoint -> DomainLocationType.stoppoint
            address -> DomainLocationType.address
            pointofinterest -> DomainLocationType.pointofinterest
            metastation -> DomainLocationType.metastation
            gps -> DomainLocationType.gps
            else -> DomainLocationType.unknown
        }
    }
}
