package com.resa.data.cache.mappers

import com.resa.global.Mapper
import com.resa.data.cache.entities.SavedJourneySearch as CacheSavedJourneySearch
import com.resa.domain.model.JourneySearch as DomainJourneySearch
import com.resa.domain.model.Location as DomainLocation

class DomainToCacheSavedJourneySearchMapper : Mapper<DomainJourneySearch, CacheSavedJourneySearch> {
    override fun map(value: DomainJourneySearch): CacheSavedJourneySearch =
        CacheSavedJourneySearch(
            id = value.id,
            originId = value.origin.id,
            originName = value.origin.name,
            originLat = value.origin.lat,
            originLon = value.origin.lon,
            originType = value.origin.type,
            destinationId = value.destination.id,
            destinationName = value.destination.name,
            destinationLat = value.destination.lat,
            destinationLon = value.destination.lon,
            destinationType = value.destination.type,
        )

    override fun reverse(value: CacheSavedJourneySearch): DomainJourneySearch =
        DomainJourneySearch(
            id = value.id,
            origin = DomainLocation(
                id = value.originId,
                name = value.originName,
                lat = value.originLat,
                lon = value.originLon,
                type = value.originType,
            ),
            destination = DomainLocation(
                id = value.destinationId,
                name = value.destinationName,
                lat = value.destinationLat,
                lon = value.destinationLon,
                type = value.destinationType,
            ),
        )
}
