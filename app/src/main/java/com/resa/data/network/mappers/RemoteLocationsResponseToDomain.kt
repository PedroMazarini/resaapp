package com.resa.data.network.mappers

import com.resa.domain.model.LocationCollection
import com.resa.domain.model.PaginationData
import com.resa.global.Mapper
import javax.inject.Inject
import com.resa.data.network.model.location.LocationsResponse as RemoteLocationsResponse

class RemoteLocationsResponseToDomain
@Inject
constructor(
    private val remoteLocationToDomain: RemoteLocationToDomain,
) : Mapper<RemoteLocationsResponse, LocationCollection> {
    override fun map(value: RemoteLocationsResponse): LocationCollection =
        LocationCollection(
            locations = value.results?.map(remoteLocationToDomain::map) ?: emptyList(),
            pagination = value.getPaginationData(),
        )
}

private fun RemoteLocationsResponse.getPaginationData(): PaginationData =
    PaginationData(
        limit = this.pagination?.limit ?: 0,
        offset = this.pagination?.offset ?: 0,
        propertySize = this.pagination?.propertySize ?: 0,
        previous = this.links?.previous.orEmpty(),
        next = this.links?.next.orEmpty(),
        current = this.links?.current.orEmpty(),
    )
