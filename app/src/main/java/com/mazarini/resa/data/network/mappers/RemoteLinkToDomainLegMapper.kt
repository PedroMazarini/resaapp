package com.mazarini.resa.data.network.mappers

import com.mazarini.resa.data.network.model.travelplanner.journeys.response.Severity
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.journey.Direction
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.domain.model.journey.LegType
import com.mazarini.resa.domain.model.journey.Warning
import com.mazarini.resa.global.Mapper
import com.mazarini.resa.global.extensions.orThrow
import com.mazarini.resa.global.extensions.parseRfc3339
import com.mazarini.resa.global.extensions.safeLet
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.ui.util.MappingException
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.ConnectionLink as RemoteLink
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.TransportMode as RemoteTransportMode
import com.mazarini.resa.domain.model.TransportMode as DomainTransportMode
import com.mazarini.resa.domain.model.journey.Leg as DomainLeg

class RemoteLinkToDomainLegMapper : Mapper<RemoteLink, DomainLeg> {
    override fun map(value: RemoteLink): DomainLeg =
        DomainLeg(
            index = value.journeyLegIndex ?: 0,
            legType = LegType.CONNECTION_LINK,
            transportMode = value.getTransportMode(),
            durationInMinutes = value.getDuration(),
            distanceInMeters = value.distanceInMeters ?: 0,
            name = value.getName(),
            warnings = value.getWarnings(),
            departTime = value.getTime(),
            direction = Direction(
                from = value.getOriginCoordinates(),
                to = value.getDestinationCoordinates(),
            ),
            directionName = value.destination?.stopPoint?.name.orEmpty(),
            details = null,
        )

    private fun RemoteLink.getWarnings(): List<Warning> {
        return notes?.map {
            Warning(
                message = it.text.orEmpty(),
                severity = it.severity?.let { severity ->
                    when (severity) {
                        Severity.normal -> com.mazarini.resa.domain.model.journey.WarningSeverity.MEDIUM
                        Severity.high -> com.mazarini.resa.domain.model.journey.WarningSeverity.HIGH
                        else -> com.mazarini.resa.domain.model.journey.WarningSeverity.LOW
                    }
                } ?: com.mazarini.resa.domain.model.journey.WarningSeverity.LOW
            )
        }.orEmpty()
    }

    private fun RemoteLink.getTime(): JourneyTimes {
        return try {
            if (isDepartureAsPlanned()) {
                com.mazarini.resa.domain.model.journey.JourneyTimes.Planned(
                    time = plannedDepartureTime?.parseRfc3339() orThrow MappingException(this),
                    isLiveTracking = false,
                )
            } else {
                com.mazarini.resa.domain.model.journey.JourneyTimes.Changed(
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
        return linkCoordinates?.first()?.let { coordinate ->
            safeLet(coordinate.latitude, coordinate.longitude) { lat, lon ->
                Coordinate(lat = lat, lon = lon)
            }
        }
    }

    private fun RemoteLink.getDestinationCoordinates(): Coordinate? {
        return linkCoordinates?.lastOrNull()?.let { coordinate ->
            safeLet(coordinate.latitude, coordinate.longitude) { lat, lon ->
                Coordinate(lat = lat, lon = lon)
            }
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
