package com.resa.data.network.mappers

import com.resa.global.Mapper
import com.resa.data.network.model.journeys.response.ConnectionLink as RemoteLink
import com.resa.data.network.model.journeys.response.TransportMode as RemoteTransportMode
import com.resa.domain.model.TransportMode as DomainTransportMode
import com.resa.domain.model.journey.Leg as DomainLeg

class RemoteLinkToDomainLegMapper : Mapper<RemoteLink, DomainLeg> {
    override fun map(value: RemoteLink): DomainLeg =
        DomainLeg.AccessLink(
            index = value.journeyLegIndex ?: 0,
            transportMode = value.getTransportMode(),
            durationInMinutes = value.getDuration(),
        )

    private fun RemoteLink.getDuration(): Int =
        estimatedDurationInMinutes ?: plannedDurationInMinutes ?: 0

    private fun RemoteLink.getTransportMode(): DomainTransportMode =
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
        private const val TAG = "RemoteLinkToDomainLegMapper"
    }
}
