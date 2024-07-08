package com.mazarini.resa.data.network.mappers

import com.mazarini.resa.data.network.model.travelplanner.journeys.response.ArrivalAccessLink
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
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.ArrivalAccessLink as RemoteArrivalLink
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.TransportMode as RemoteTransportMode
import com.mazarini.resa.domain.model.TransportMode as DomainTransportMode
import com.mazarini.resa.domain.model.journey.Leg as DomainLeg

const val ARRIVAL_INDEX = 9999

class RemoteArrivalLinkToDomainLegMapper : Mapper<RemoteArrivalLink, DomainLeg> {
    override fun map(value: RemoteArrivalLink): DomainLeg =
        DomainLeg(
            index = ARRIVAL_INDEX,
            legType = LegType.ARRIVAL_LINK,
            transportMode = value.getTransportMode(),
            durationInMinutes = value.getDuration(),
            distanceInMeters = value.distanceInMeters ?: 0,
            name = value.origin?.stopPoint?.name.orEmpty(),
            warnings = value.getWarnings(),
            departTime = value.getTime(),
            direction = Direction(
                from = value.getOriginCoordinates(),
                to = value.getDestinationCoordinates(),
            ),
            directionName = value.destination?.name.orEmpty(),
            details = null,
        )

    private fun RemoteArrivalLink.getWarnings(): List<Warning> {
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

    private fun ArrivalAccessLink.isDepartureAsPlanned(): Boolean =
        (estimatedDepartureTime == null) || (plannedDepartureTime == estimatedDepartureTime)

    private fun ArrivalAccessLink.getTime(): JourneyTimes {
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
            loge("${com.mazarini.resa.data.network.mappers.RemoteArrivalLinkToDomainLegMapper.TAG} failed to map Time: ${e.message}")
            error(e)
        }
    }


    private fun RemoteArrivalLink.getOriginCoordinates(): Coordinate? {
        return linkCoordinates?.first()?.let { coordinate ->
            safeLet(coordinate.latitude, coordinate.longitude) { lat, lon ->
                Coordinate(lat = lat, lon = lon)
            }
        }
    }

    private fun RemoteArrivalLink.getDestinationCoordinates(): Coordinate? {
        return linkCoordinates?.lastOrNull()?.let { coordinate ->
            safeLet(coordinate.latitude, coordinate.longitude) { lat, lon ->
                Coordinate(lat = lat, lon = lon)
            }
        }
    }

    private fun RemoteArrivalLink.getDuration(): Int =
        estimatedDurationInMinutes ?: plannedDurationInMinutes ?: 0

    private fun RemoteArrivalLink.getTransportMode(): DomainTransportMode =
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
        private const val TAG = "RemoteArrivalLinkToDomainLegMapper"
    }
}

fun ArrivalAccessLink.getArrivalTime(): JourneyTimes {
    return try {
        if (isArrivalAsPlanned()) {
            com.mazarini.resa.domain.model.journey.JourneyTimes.Planned(
                time = plannedArrivalTime?.parseRfc3339() orThrow MappingException(this),
                isLiveTracking = false,
            )
        } else {
            com.mazarini.resa.domain.model.journey.JourneyTimes.Changed(
                planned = plannedArrivalTime?.parseRfc3339() orThrow MappingException(this),
                estimated = estimatedArrivalTime?.parseRfc3339() orThrow MappingException(this),
                isLiveTracking = true,
            )
        }
    } catch (e: Exception) {
        loge("RemoteArrivalLinkToDomainMapper failed to map Time: ${e.message}")
        error(e)
    }
}

private fun ArrivalAccessLink.isArrivalAsPlanned(): Boolean =
    (estimatedArrivalTime == null) || (plannedArrivalTime == estimatedArrivalTime)
