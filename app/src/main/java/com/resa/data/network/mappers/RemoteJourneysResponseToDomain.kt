package com.resa.data.network.mappers

import com.resa.data.network.model.journeys.GetJourneysResponse
import com.resa.domain.model.PaginationData
import com.resa.global.Mapper
import javax.inject.Inject
import com.resa.data.network.model.journeys.GetJourneysResponse as RemoteJourneysResponse
import com.resa.domain.model.JourneysCollection as DomainJourneysResponse

class RemoteJourneysResponseToDomain
@Inject
constructor(
    private val remoteJourneyToDomainJourneyMapper: RemoteJourneyToDomainJourneyMapper,
) : Mapper<RemoteJourneysResponse, DomainJourneysResponse> {
    override fun map(value: RemoteJourneysResponse): DomainJourneysResponse =
        DomainJourneysResponse(
            journeys = value.results?.map(remoteJourneyToDomainJourneyMapper::map) ?: emptyList(),
            pagination = value.getPaginationData(),
        )
}

private fun GetJourneysResponse.getPaginationData(): PaginationData =
    PaginationData(
        limit = this.pagination?.limit ?: 0,
        offset = this.pagination?.offset ?: 0,
        propertySize = this.pagination?.propertySize ?: 0,
        previous = this.links?.previous.orEmpty(),
        next = this.links?.next.orEmpty(),
        current = this.links?.current.orEmpty(),
    )
