package com.resa.data.network.mappers

import androidx.compose.ui.graphics.Color
import com.resa.data.network.model.travelplanner.journeys.response.ServiceJourney
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.LegColors
import com.resa.domain.model.stoparea.StopJourney
import com.resa.domain.model.stoparea.StopPoint as DomainStopPoint
import com.resa.global.extensions.asColor
import com.resa.global.extensions.orFalse
import com.resa.global.extensions.parseRfc3339
import com.resa.global.loge
import com.resa.ui.theme.colors.FrenchBlue
import com.resa.data.network.model.travelplanner.journeys.response.TransportMode as RemoteTransportMode
import com.resa.data.network.model.travelplanner.stopareas.StopDepartureOrArrival as RemoteStopJourney
import com.resa.domain.model.TransportMode as DomainTransportMode

typealias RemoteDepartOrArrive = List<RemoteStopJourney>
typealias DomainStopPoints = List<DomainStopPoint>

class RemoteToDomainStopPointsMapper {

    private val stopPointMap = mutableMapOf<String, DomainStopPoint>()

    fun map(value: RemoteDepartOrArrive): DomainStopPoints {
        try {
            value.forEach { departOrArrive ->
                stopPointMap.putIfAbsent(
                    departOrArrive.stopPoint.gid,
                    DomainStopPoint(
                        gid = departOrArrive.stopPoint.gid,
                        name = departOrArrive.stopPoint.name,
                        platform = departOrArrive.stopPoint.platform.orEmpty(),
                        latitude = departOrArrive.stopPoint.latitude ?: 0.0,
                        longitude = departOrArrive.stopPoint.longitude ?: 0.0,
                    ),
                )
                stopPointMap[departOrArrive.stopPoint.gid]?.let { stopPoint ->
                    stopPointMap[departOrArrive.stopPoint.gid] = stopPoint.copy(
                        departures = stopPoint.departures + departOrArrive.getStopJourney(),
                    )
                }
            }
        } catch (e: Exception) {
            loge("$TAG ${e.message.toString()}")
        }

        return stopPointMap.values.toList()
    }

    private fun RemoteStopJourney.getStopJourney(): StopJourney =
        StopJourney(
            time = getStopJourneyTime(),
            isCancelled = isCancelled.orFalse,
            isPartCancelled = isPartCancelled.orFalse,
            direction = serviceJourney?.direction ?: error("Direction could not be parsed"),
            origin = serviceJourney.origin.orEmpty(),
            number = serviceJourney.number.orEmpty(),
            colors = getStopJourneyColors(),
            transportMode = serviceJourney.getTransportMode(),
            shortName = serviceJourney.line?.shortName.orEmpty(),
            hasAccessibility = serviceJourney.line?.isWheelchairAccessible.orFalse,
        )

    private fun ServiceJourney.getTransportMode(): DomainTransportMode =
        when (line?.transportMode) {
            RemoteTransportMode.bus -> DomainTransportMode.bus
            RemoteTransportMode.train -> DomainTransportMode.train
            RemoteTransportMode.tram -> DomainTransportMode.tram
            RemoteTransportMode.ferry -> DomainTransportMode.ferry
            RemoteTransportMode.walk -> DomainTransportMode.walk
            RemoteTransportMode.bike -> DomainTransportMode.bike
            RemoteTransportMode.car -> DomainTransportMode.car
            RemoteTransportMode.taxi -> DomainTransportMode.taxi
            else -> DomainTransportMode.unknown
        }

    private fun RemoteStopJourney.getStopJourneyColors(): LegColors =
        try {
            LegColors(
                foreground = serviceJourney?.line?.foregroundColor?.asColor()
                    ?: error("No foreground color"),
                background = serviceJourney.line.backgroundColor?.asColor()
                    ?: error("No background color"),
                border = serviceJourney.line.borderColor?.asColor()
                    ?: error("No border color"),
            )
        } catch (e: Exception) {
            loge("$TAG ${e.message.toString()}")
            LegColors(
                foreground = Color.White,
                background = FrenchBlue,
                border = FrenchBlue,
            )
        }


    private fun RemoteStopJourney.getStopJourneyTime(): JourneyTimes {
        return when (estimatedTime) {
            estimatedOtherwisePlannedTime -> JourneyTimes.Changed(
                estimated = estimatedTime?.parseRfc3339()
                    ?: error("Journey time could not be parsed"),
                planned = plannedTime.parseRfc3339() ?: error("Journey time could not be parsed"),
                isLiveTracking = true,
            )

            else -> JourneyTimes.Planned(
                time = estimatedTime?.parseRfc3339() ?: error("Journey time could not be parsed"),
                isLiveTracking = false,
            )
        }
    }

    companion object {
        private const val TAG = "RemoteToDomainStopAreasMapper"
    }
}

