package com.resa.data.network.mappers

import androidx.compose.ui.graphics.Color
import com.resa.domain.model.journey.LegColors
import com.resa.global.Mapper
import com.resa.global.extensions.asColor
import com.resa.global.loge
import com.resa.ui.theme.colors.FrenchBlue
import com.resa.data.network.model.travelplanner.journeys.response.TransportMode as RemoteTransportMode
import com.resa.data.network.model.travelplanner.journeys.response.TripLeg as RemoteLeg
import com.resa.domain.model.TransportMode as DomainTransportMode
import com.resa.domain.model.journey.Leg as DomainLeg
import com.resa.domain.model.journey.LegColors as DomainLegColors

class RemoteToDomainLegMapper : Mapper<RemoteLeg, DomainLeg> {
    override fun map(value: RemoteLeg): DomainLeg =
        DomainLeg.Transport(
            index = value.journeyLegIndex ?: 0,
            name = value.getLineName(),
            transportMode = value.getTransportMode(),
            durationInMinutes = value.getDuration(),
            colors = value.getColors(),
        )

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
            loge("$TAG failed to map Leg Colors using default: ${e.message}")
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
