package com.resa.domain.model

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
}