package com.mazarini.resa.data.cache.mappers

import com.mazarini.resa.global.Mapper
import com.mazarini.resa.data.cache.entities.RecentLocation as CacheRecentLocation
import com.mazarini.resa.domain.model.Location as DomainLocation

class DomainToCacheRecentLocationMapper : Mapper<DomainLocation, CacheRecentLocation> {
    override fun map(value: DomainLocation): CacheRecentLocation =
        CacheRecentLocation(
            id = value.id,
            name = value.name,
            lat = value.lat,
            lon = value.lon,
            type = value.type,
        )

    override fun reverse(value: CacheRecentLocation): DomainLocation =
        DomainLocation(
            id = value.id,
            name = value.name,
            lat = value.lat,
            lon = value.lon,
            type = value.type,
        )
}
