package com.resa.data.network.mappers

import com.resa.global.Mapper
import com.resa.data.network.model.location.Location as RemoteLocation
import com.resa.domain.model.Location as DomainLocation

class RemoteLocationToDomain : Mapper<RemoteLocation, DomainLocation> {
    override fun map(value: RemoteLocation): DomainLocation =
        DomainLocation(
            name = value.name,
        )
}
