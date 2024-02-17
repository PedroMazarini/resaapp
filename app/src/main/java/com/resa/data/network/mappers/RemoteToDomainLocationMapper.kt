package com.resa.data.network.mappers

import com.resa.global.Mapper
import com.resa.data.network.model.travelplanner.location.Location as RemoteLocation
import com.resa.domain.model.Location as DomainLocation
import com.resa.domain.model.LocationType as DomainLocationType

class RemoteToDomainLocationMapper : Mapper<RemoteLocation, DomainLocation> {
    override fun map(value: RemoteLocation): DomainLocation =
        DomainLocation(
            id = value.gid.orEmpty(),
            name = value.name,
            lat = value.latitude,
            lon = value.longitude,
            type = DomainLocationType fromString value.locationType.name,
        )
}
