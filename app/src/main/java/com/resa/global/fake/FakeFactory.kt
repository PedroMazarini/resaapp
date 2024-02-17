package com.resa.global.fake

import androidx.compose.ui.graphics.Color
import com.resa.domain.model.TransportMode
import com.resa.domain.model.journey.Departure
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.Leg
import com.resa.domain.model.journey.LegColors
import com.resa.domain.model.journey.OccupancyLevel
import com.resa.domain.model.journey.WarningTypes
import com.resa.domain.model.stoparea.StopJourney
import com.resa.domain.model.stoparea.StopPoint
import com.resa.global.extensions.plusSec
import com.resa.ui.model.JourneySearch
import com.resa.ui.model.Location
import com.resa.ui.model.LocationType
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
        isLive: Boolean = true,
    ): Journey {
        return Journey(
            arrivalTimes = JourneyTimes.Changed(
                planned = Date(),
                estimated = Date(),
                isLiveTracking = isLive,
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
            occupancyLevel = OccupancyLevel.MEDIUM,
            departure = Departure(
                time = JourneyTimes.Planned(
                    time = Date(),
//                    planned = Date(),
//                    estimated = Date().plusSec(3700),
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

    fun journeySearchList(count: Int = 10): List<JourneySearch> {
        val result = mutableListOf<JourneySearch>()
        for (i in 0..count) {
            result.add(
                journeySearch()
            )
        }
        return result.toList()
    }

    fun journeySearch(): JourneySearch =
        JourneySearch(
            id = 1,
            origin = Location(
                id = UUID.randomUUID().toString(),
                name = "Liseberg long name that should ",
                type = LocationType.values().random()
            ),
            destination = Location(
                id = UUID.randomUUID().toString(),
                name = "Brunnsparken long name ipsis literis",
                type = LocationType.values().random()
            )
        )

    fun stopPointList(count: Int = 10): List<StopPoint> {
        val result = mutableListOf<StopPoint>()
        for (i in 0..count) {
            result.add(
                StopPoint(
                    gid = UUID.randomUUID().toString(),
                    name = "Liseberg $i",
                    platform = "A$i",
                    latitude = 57.696994,
                    longitude = 11.9865,
                    departures = stopJourneyList(),
                    arrivals = stopJourneyList(),
                )
            )
        }
        return result.toList()
    }
    fun stopJourneyList(count: Int = 10): List<StopJourney> {
        val result = mutableListOf<StopJourney>()
        for (i in 0..count) {
            result.add(
                StopJourney(
                    time = JourneyTimes.Planned(
                        time = Date(),
                        isLiveTracking = true,
                    ),
                    isCancelled = false,
                    isPartCancelled = false,
                    direction = "Liseberg",
                    origin = "Brunnsparken",
                    number = i.toString(),
                    colors = LegColors(
                        foreground = Color.White,
                        background = Color.Blue,
                        border = Color.White,
                    ),
                    transportMode = TransportMode.bus,
                    shortName = i.toString(),
                    hasAccessibility = true,
                )
            )
        }
        return result.toList()
    }
}
