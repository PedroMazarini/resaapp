package com.mazarini.resa.data.network.mappers

import androidx.compose.ui.graphics.Color
import com.mazarini.resa.data.network.model.travelplanner.position.Position
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.journey.LegColors
import com.mazarini.resa.global.Mapper
import com.mazarini.resa.global.extensions.asColor
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.ui.theme.colors.FrenchBlue
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.TransportMode as RemoteTransportMode
import com.mazarini.resa.domain.model.TransportMode as DomainTransportMode


class RemoteToDomainVehicleMapper : Mapper<Position, VehiclePosition> {
    override fun map(value: Position): VehiclePosition =
        VehiclePosition(
            name = value.name.orEmpty(),
            position = value.getCoordinates(),
            colors = value.getColors(),
            directionName = value.direction.orEmpty(),
            transportMode = value.getTransportMode(),
        )

    private fun Position.getTransportMode(): DomainTransportMode =
        line?.transportMode.let { mode ->
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
                else -> DomainTransportMode.unknown
            }
        }

    private fun Position.getColors(): LegColors {
        return try {
            LegColors(
                foreground = line?.foregroundColor?.asColor()
                    ?: error("No foreground color"),
                background = line.backgroundColor?.asColor()
                    ?: error("No background color"),
                border = line.borderColor?.asColor() ?: error("No border color"),
            )
        } catch (e: Exception) {
            loge("failed to map Leg Colors using default: ${e.message}")
            LegColors(
                foreground = Color.White,
                background = FrenchBlue,
                border = FrenchBlue,
            )
        }
    }

    private fun Position.getCoordinates(): Coordinate =
        Coordinate(
            lat = latitude ?: 0.0,
            lon = longitude ?: 0.0,
        )
}
