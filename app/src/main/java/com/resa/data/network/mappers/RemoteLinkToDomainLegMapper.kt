package com.resa.data.network.mappers

import com.resa.data.network.model.travelplanner.journeys.response.ArrivalAccessLink
import com.resa.data.network.model.travelplanner.journeys.response.DepartureAccessLink
import com.resa.data.network.model.travelplanner.journeys.response.Severity
import com.resa.domain.model.Coordinate
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.Warning
import com.resa.domain.model.journey.WarningSeverity
import com.resa.global.Mapper
import com.resa.global.extensions.orThrow
import com.resa.global.extensions.parseRfc3339
import com.resa.global.extensions.safeLet
import com.resa.global.loge
import com.resa.ui.util.MappingException
import com.resa.data.network.model.travelplanner.journeys.response.ConnectionLink as RemoteLink
import com.resa.data.network.model.travelplanner.journeys.response.TransportMode as RemoteTransportMode
import com.resa.domain.model.TransportMode as DomainTransportMode
import com.resa.domain.model.journey.Leg as DomainLeg

class RemoteLinkToDomainLegMapper : Mapper<RemoteLink, DomainLeg> {
    override fun map(value: RemoteLink): DomainLeg =
        DomainLeg.AccessLink(
            index = value.journeyLegIndex ?: 0,
            transportMode = value.getTransportMode(),
            durationInMinutes = value.getDuration(),
            distanceInMeters = value.distanceInMeters ?: 0,
            name = value.getName(),
            warnings = value.getWarnings(),
            departTime = value.getTime(),
            from = value.getOriginCoordinates(),
            to = value.getDestinationCoordinates(),
        )

    private fun RemoteLink.getWarnings(): List<Warning> {
        return notes?.map {
            Warning(
                message = it.text.orEmpty(),
                severity = it.severity?.let { severity ->
                    when (severity) {
                        Severity.normal -> WarningSeverity.MEDIUM
                        Severity.high -> WarningSeverity.HIGH
                        else -> WarningSeverity.LOW
                    }
                } ?: WarningSeverity.LOW
            )
        }.orEmpty()
    }

    private fun RemoteLink.getTime(): JourneyTimes {
        return try {
            if (isDepartureAsPlanned()) {
                JourneyTimes.Planned(
                    time = plannedDepartureTime?.parseRfc3339() orThrow MappingException(this),
                    isLiveTracking = false,
                )
            } else {
                JourneyTimes.Changed(
                    planned = plannedDepartureTime?.parseRfc3339() orThrow MappingException(this),
                    estimated = estimatedDepartureTime?.parseRfc3339() orThrow MappingException(this),
                    isLiveTracking = true,
                )
            }
        } catch (e: Exception) {
            loge("TAG failed to map Time: ${e.message}")
            error(e)
        }
    }
    private fun RemoteLink.isDepartureAsPlanned(): Boolean =
        (estimatedDepartureTime == null) || (plannedDepartureTime == estimatedDepartureTime)

    private fun RemoteLink.getName(): String {
        return origin?.stopPoint?.name.orEmpty()
    }
    private fun RemoteLink.getOriginCoordinates(): Coordinate? {
        return safeLet(origin?.stopPoint?.latitude, origin?.stopPoint?.longitude) { lat, lon ->
            Coordinate(lat = lat, lon = lon)
        }
    }

    private fun RemoteLink.getDestinationCoordinates(): Coordinate? {
        return safeLet(
            destination?.stopPoint?.latitude,
            destination?.stopPoint?.longitude
        ) { lat, lon ->
            Coordinate(lat = lat, lon = lon)
        }
    }

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
