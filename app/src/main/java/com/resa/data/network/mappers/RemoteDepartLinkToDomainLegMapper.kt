package com.resa.data.network.mappers

import com.resa.global.Mapper
import com.resa.data.network.model.travelplanner.journeys.response.DepartureAccessLink as RemoteDepartLink
import com.resa.data.network.model.travelplanner.journeys.response.TransportMode as RemoteTransportMode
import com.resa.domain.model.TransportMode as DomainTransportMode
import com.resa.domain.model.journey.Leg as DomainLeg

class RemoteDepartLinkToDomainLegMapper : Mapper<RemoteDepartLink, DomainLeg> {
    override fun map(value: RemoteDepartLink): DomainLeg =
        DomainLeg.AccessLink(
            index = 0,
            transportMode = value.getTransportMode(),
            durationInMinutes = value.getDuration(),
        )

    private fun RemoteDepartLink.getDuration(): Int =
        estimatedDurationInMinutes ?: plannedDurationInMinutes ?: 0

    private fun RemoteDepartLink.getTransportMode(): DomainTransportMode =
        transportMode?.let { mode ->
            when (mode) {
                RemoteTransportMode.unknown -> DomainTransportMode.unknown
                RemoteTransportMode.none -> DomainTransportMode.none
                RemoteTransportMode.tram -> DomainTransportMode.tram
                RemoteTransportMode.bus -> DomainTransportMode.bus
                RemoteTransportMode.ferry -> DomainTransportMode.ferry
                RemoteTransportMode.train -> DomainTransportMode.train
                RemoteTransportMode.taxi -> DomainTransportMode.taxi
                RemoteTransportMode.walk -> DomainTransportMode.walk
                RemoteTransportMode.bike -> DomainTransportMode.bike
                RemoteTransportMode.car -> DomainTransportMode.car
            }
        } ?: DomainTransportMode.unknown

    companion object {
        private const val TAG = "RemoteDepartLinkToDomainLegMapper"
    }
}
