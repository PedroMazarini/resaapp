package com.resa.domain.model

enum class LocationType {
    unknown,
    stoparea,
    stoppoint,
    address,
    pointofinterest,
    metastation;

    companion object {
        infix fun fromString(value: String): LocationType {
            return LocationType.valueOf(value)
        }
    }
}