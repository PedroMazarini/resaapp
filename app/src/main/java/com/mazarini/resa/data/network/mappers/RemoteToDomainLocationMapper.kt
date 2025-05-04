package com.mazarini.resa.data.network.mappers

import com.mazarini.resa.domain.model.LocationType
import com.mazarini.resa.global.Mapper
import com.mazarini.resa.data.network.model.travelplanner.location.Location as RemoteLocation
import com.mazarini.resa.domain.model.Location as DomainLocation

class RemoteToDomainLocationMapper : Mapper<RemoteLocation, DomainLocation> {
    override fun map(value: RemoteLocation): DomainLocation =
        DomainLocation(
            id = value.getId(),
            name = value.name,
            lat = value.latitude,
            lon = value.longitude,
            type = LocationType fromString value.locationType.name,
        )

    private fun RemoteLocation.getId(): String =
        gid ?: run {
            "${latitude}${longitude}"
        }
}
