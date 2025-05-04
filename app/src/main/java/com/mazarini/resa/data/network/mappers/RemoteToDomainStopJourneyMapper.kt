package com.mazarini.resa.data.network.mappers

import androidx.compose.ui.graphics.Color
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.ServiceJourney
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.domain.model.journey.LegColors
import com.mazarini.resa.domain.model.stoparea.StopJourney as DomainStopJourney
import com.mazarini.resa.global.extensions.asColor
import com.mazarini.resa.global.extensions.orFalse
import com.mazarini.resa.global.extensions.parseRfc3339
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.TransportMode as RemoteTransportMode
import com.mazarini.resa.data.network.model.travelplanner.stopareas.StopDepartureOrArrival as RemoteStopJourney
import com.mazarini.resa.domain.model.TransportMode as DomainTransportMode

class RemoteToDomainStopJourneyMapper {

    fun map(value: RemoteStopJourney): DomainStopJourney =
        DomainStopJourney(
            time = value.getStopJourneyTime(),
            direction = value.serviceJourney?.direction ?: error("Direction could not be parsed"),
            origin = value.serviceJourney.origin.orEmpty(),
            colors = value.getStopJourneyColors(),
            transportMode = value.serviceJourney.getTransportMode(),
            shortName = value.serviceJourney.line?.shortName.orEmpty(),
            hasAccessibility = value.serviceJourney.line?.isWheelchairAccessible.orFalse,
            platform = value.stopPoint.platform.orEmpty(),
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
                background = com.mazarini.resa.ui.theme.colors.FrenchBlue,
                border = com.mazarini.resa.ui.theme.colors.FrenchBlue,
            )
        }


    private fun RemoteStopJourney.getStopJourneyTime(): JourneyTimes {
        return when (estimatedTime) {
            estimatedOtherwisePlannedTime -> JourneyTimes.Changed(
                estimated = estimatedTime?.parseRfc3339()
                    ?: error("Journey time could not be parsed"),
                planned = plannedTime.parseRfc3339() ?: error("Estimated journey time could not be parsed ($estimatedTime)"),
                isLiveTracking = true,
            )

            else -> JourneyTimes.Planned(
                time = plannedTime.parseRfc3339() ?: error("Planned journey time could not be parsed ($plannedTime)"),
                isLiveTracking = false,
            )
        }
    }

    companion object {
        private const val TAG = "RemoteToDomainStopAreasMapper"
    }
}

