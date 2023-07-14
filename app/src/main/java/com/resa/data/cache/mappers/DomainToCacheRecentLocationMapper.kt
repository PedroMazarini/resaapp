package com.resa.data.cache.mappers

import com.resa.global.Mapper
import com.resa.data.cache.entities.RecentLocation as CacheRecentLocation
import com.resa.domain.model.Location as DomainLocation

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
