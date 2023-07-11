package com.resa.global.fake

import androidx.compose.ui.graphics.Color
import com.resa.domain.model.TransportMode
import com.resa.global.extensions.plusSec
import com.resa.ui.model.Location
import com.resa.ui.model.LocationType
import com.resa.domain.model.journey.Departure
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.Leg
import com.resa.domain.model.journey.LegColors
import com.resa.domain.model.journey.WarningTypes
import java.util.Date
import java.util.UUID

object FakeFactory {

    fun locationList(count: Int = 10): List<Location> {
        val result = mutableListOf<Location>()
        for (i in 0..count) {
            result.add(
                Location(
                    id = UUID.randomUUID().toString(),
                    name = "Liseberg $i",
                    type = LocationType.values().random()
                )
            )
        }
        return result.toList()
    }

    fun leg(
        name: String = "1",
        mode: TransportMode = TransportMode.walk,
    ): Leg {
        return Leg.Transport(
            index = 0,
            name = name,
            transportMode = mode,
            durationInMinutes = 10,
            colors = LegColors(
                foreground = Color.White,
                background = Color.Blue,
                border = Color.White,
            ),
        )
    }

    fun journey(
        departStopName: String = "Liseberg",
        departPlatform: String = "A",
    ): Journey {
        return Journey(
            arrivalTimes = JourneyTimes.Changed(
                planned = Date(),
                estimated = Date(),
                isLiveTracking = true,
            ),
            durationInMinutes = 45,
            legs = listOf(
                leg(name = "1"),
                leg(name = "2", mode = TransportMode.bus),
                leg(name = "3", mode = TransportMode.tram),
                leg(name = "4"),
                leg(name = "5", mode = TransportMode.ferry),
                leg(name = "6"),
            ),
            hasAccessibility = true,
            warning = WarningTypes.MediumWarning,
            isDeparted = true,
            id = UUID.randomUUID().toString(),
            departure = Departure(
                time = JourneyTimes.Changed(
                    planned = Date(),
                    estimated = Date().plusSec(3700),
                    isLiveTracking = true,
                ),
                departStopName = departStopName,
                departPlatform = departPlatform,
            )
        )
    }

    fun journeyList(count: Int = 10): List<Journey> {
        val result = mutableListOf<Journey>()
        for (i in 0..count) {
            result.add(
                journey(departStopName = "Liseberg $i", departPlatform = "A$i")
            )
        }
        return result.toList()
    }
}
