package com.resa.data.cache.mappers

import com.resa.global.Mapper
import com.resa.data.cache.entities.SavedLocation as CacheSavedLocation
import com.resa.domain.model.Location as DomainLocation

class DomainToCacheSavedLocationMapper : Mapper<DomainLocation, CacheSavedLocation> {
    override fun map(value: DomainLocation): CacheSavedLocation =
        CacheSavedLocation(
            id = value.id,
            name = value.name,
            lat = value.lat,
            lon = value.lon,
            type = value.type,
        )

    override fun reverse(value: CacheSavedLocation): DomainLocation =
        DomainLocation(
            id = value.id,
            name = value.name,
            lat = value.lat,
            lon = value.lon,
            type = value.type,
        )
}
