package com.resa.data.network.mappers

import com.resa.global.Mapper
import com.resa.global.extensions.orFalse
import com.resa.data.network.model.journeys.response.Journey as RemoteJourney
import com.resa.domain.model.Journey as DomainJourney

class RemoteJourneyToDomainJourneyMapper : Mapper<RemoteJourney, DomainJourney> {
    override fun map(value: RemoteJourney): DomainJourney =
        DomainJourney(
            isDeparted = value.isDeparted.orFalse,
            name = value.tripLegs?.first()?.serviceJourney?.line?.name ?: "Nao veio",
        )
}
