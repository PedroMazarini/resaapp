package com.mazarini.resa.data.cache.mappers

import com.mazarini.resa.global.Mapper
import com.mazarini.resa.data.cache.entities.SavedLocation as CacheSavedLocation
import com.mazarini.resa.domain.model.Location as DomainLocation

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
