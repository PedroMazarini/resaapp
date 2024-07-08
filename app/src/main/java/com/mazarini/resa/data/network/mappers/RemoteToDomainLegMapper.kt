package com.mazarini.resa.data.network.mappers

import androidx.compose.ui.graphics.Color
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.Severity
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.journey.Direction
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.domain.model.journey.LegColors
import com.mazarini.resa.domain.model.journey.LegType
import com.mazarini.resa.domain.model.journey.Warning
import com.mazarini.resa.global.Mapper
import com.mazarini.resa.global.extensions.asColor
import com.mazarini.resa.global.extensions.orThrow
import com.mazarini.resa.global.extensions.parseRfc3339
import com.mazarini.resa.global.extensions.safeLet
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.ui.theme.colors.FrenchBlue
import com.mazarini.resa.ui.util.MappingException
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.TransportMode as RemoteTransportMode
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.TripLeg as RemoteLeg
import com.mazarini.resa.domain.model.TransportMode as DomainTransportMode
import com.mazarini.resa.domain.model.journey.Leg as DomainLeg
import com.mazarini.resa.domain.model.journey.LegColors as DomainLegColors

class RemoteToDomainLegMapper : Mapper<RemoteLeg, DomainLeg> {
    override fun map(value: RemoteLeg): DomainLeg =
        DomainLeg(
            index = value.journeyLegIndex ?: 0,
            legType = LegType.TRANSPORT,
            name = value.getLineName(),
            transportMode = value.getTransportMode(),
            durationInMinutes = value.getDuration(),
            departTime = value.getTime(),
            warnings = value.getWarnings(),
            direction = Direction(
                from = value.getOriginCoordinates(),
                to = value.getDestinationCoordinates(),
            ),
            directionName = value.serviceJourney?.direction.orEmpty(),
            distanceInMeters = value.estimatedDistanceInMeters ?: 0,
            colors = value.getColors(),
            details = null,
        )

    private fun RemoteLeg.getWarnings(): List<Warning> {
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

    private fun RemoteLeg.getOriginCoordinates(): Coordinate? {
        return origin.stopPoint.let { stopPoint ->
            safeLet(stopPoint.latitude, stopPoint.longitude) { lat, lon ->
                Coordinate(lat = lat, lon = lon)
            }
        }
    }

    private fun RemoteLeg.getDestinationCoordinates(): Coordinate? {
        return destination.stopPoint.let { stopPoint ->
            safeLet(stopPoint.latitude, stopPoint.longitude) { lat, lon ->
                Coordinate(lat = lat, lon = lon)
            }
        }
    }

    private fun RemoteLeg.getTime(): JourneyTimes {
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
            loge("${com.mazarini.resa.data.network.mappers.RemoteToDomainLegMapper.TAG} failed to map Time: ${e.message}")
            error(e)
        }
    }

    private fun RemoteLeg.isDepartureAsPlanned(): Boolean =
        plannedDepartureTime == estimatedOtherwisePlannedDepartureTime

    private fun RemoteLeg.getDuration(): Int =
        estimatedDurationInMinutes ?: plannedDurationInMinutes ?: 0

    private fun RemoteLeg.getLineName(): String =
        serviceJourney?.line?.shortName.orEmpty()

    private fun RemoteLeg.getColors(): LegColors {
        return try {
            DomainLegColors(
                foreground = serviceJourney?.line?.foregroundColor?.asColor()
                    ?: error("No foreground color"),
                background = serviceJourney.line.backgroundColor?.asColor()
                    ?: error("No background color"),
                border = serviceJourney.line.borderColor?.asColor() ?: error("No border color"),
            )
        } catch (e: Exception) {
            loge("${com.mazarini.resa.data.network.mappers.RemoteToDomainLegMapper.TAG} failed to map Leg Colors using default: ${e.message}")
            DomainLegColors(
                foreground = Color.White,
                background = FrenchBlue,
                border = FrenchBlue,
            )
        }
    }

    private fun RemoteLeg.getTransportMode(): DomainTransportMode =
        serviceJourney?.line?.transportMode?.let { mode ->
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
        private const val TAG = "RemoteToDomainLegMapper"
    }
}
