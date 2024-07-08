package com.mazarini.resa.global.fake

import androidx.compose.ui.graphics.Color
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.journey.Destination
import com.mazarini.resa.domain.model.journey.Departure
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.domain.model.journey.Leg
import com.mazarini.resa.domain.model.journey.LegColors
import com.mazarini.resa.domain.model.journey.LegDetails
import com.mazarini.resa.domain.model.journey.LegStop
import com.mazarini.resa.domain.model.journey.LegType
import com.mazarini.resa.domain.model.journey.OccupancyLevel
import com.mazarini.resa.domain.model.journey.Platform
import com.mazarini.resa.domain.model.journey.Warning
import com.mazarini.resa.domain.model.journey.WarningSeverity
import com.mazarini.resa.domain.model.journey.WarningTypes
import com.mazarini.resa.domain.model.stoparea.StopJourney
import com.mazarini.resa.ui.model.JourneySearch
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.model.LocationType
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

    fun legList(count: Int = 10): List<Leg> {
        val result = mutableListOf<Leg>()
        for (i in 0..count) result.add(leg())
        return result.toList()
    }

    fun leg(
        index : Int = 0,
        name: String = "1",
        mode: TransportMode = TransportMode.walk,
        details: LegDetails? = legDetails(),
        departTime: JourneyTimes = JourneyTimes.Planned(
            time = Date(),
            isLiveTracking = true,
        ),
        warnings: List<Warning> = listOf(),
        legType: LegType = LegType.TRANSPORT,
    ): Leg {
        return Leg(
            index = index,
            name = name,
            transportMode = mode,
            durationInMinutes = 10,
            departTime = departTime,
            details = details,
            distanceInMeters = 720,
            directionName = "Backa",
            warnings = warnings,
            direction = null,
            legType = legType,
        )
    }

    fun legDetails(): LegDetails =
        LegDetails(
            index = 0,
            isLastLeg = false,
            platForm = Platform.Planned("A"),
            destination = Destination(
                name = "Liseberg",
                platform = Platform.Planned("B"),
                arrivalTime = JourneyTimes.Planned(Date(), true)),
            legStops = listOf(),
            pathWay = listOf(),
        )

    fun departWalkLeg(
        departTime: JourneyTimes = JourneyTimes.Planned(
            time = Date(),
            isLiveTracking = true,
        ),
        warnings: List<Warning> = listOf(),
    ): Leg {
        return Leg (
            index = 0,
            transportMode = TransportMode.walk,
            durationInMinutes = 10,
            name = "Liseberg",
            departTime = departTime,
            directionName = "Backa",
            distanceInMeters = 720,
            direction = null,
            warnings = warnings,
            details = null,
            legType = LegType.DEPART_LINK,
        )
    }

    fun accessWalkLeg(): Leg {
        return Leg(
            index = 0,
            transportMode = TransportMode.walk,
            durationInMinutes = 10,
            distanceInMeters = 720,
            name = "Liseberg",
            departTime = JourneyTimes.Planned(
                time = Date(),
                isLiveTracking = true,
            ),
            direction = null,
            directionName = "Backa",
            warnings = listOf(),
            details = LegDetails(
                index = 0,
                isLastLeg = false,
                platForm = null,
            ),
            legType = LegType.CONNECTION_LINK,
        )
    }

    fun arrivalWalkLeg(): Leg {
        return Leg(
            index = 0,
            transportMode = TransportMode.walk,
            durationInMinutes = 10,
            distanceInMeters = 720,
            name = "Smörkärnegatan 1",
            departTime = JourneyTimes.Planned(time = Date(), isLiveTracking = true),
            direction = null,
            directionName = "Backa",
            warnings = listOf(),
            details = LegDetails(
                index = 0,
                isLastLeg = true,
                platForm = Platform.Planned("A"),
                destination = Destination(
                    name = "Smörkärnegatan 29",
                    platform = Platform.Planned("A"),
                    arrivalTime = JourneyTimes.Planned(Date(), true),
                ),
            ),
            legType = LegType.ARRIVAL_LINK,
        )
    }

    fun journey(
        departStopName: String = "Liseberg",
        departPlatform: String = "A",
        isLive: Boolean = true,
        legs: List<Leg> = listOf(
            leg(name = "1"),
            leg(name = "2", mode = TransportMode.bus),
            leg(name = "3", mode = TransportMode.tram),
            leg(name = "4"),
            leg(name = "5", mode = TransportMode.ferry),
            leg(name = "6"),
        ),
    ): Journey {
        return Journey(
            arrivalTimes = JourneyTimes.Changed(
                planned = Date(),
                estimated = Date(),
                isLiveTracking = isLive,
            ),
            durationInMinutes = 45,
            legs = legs,
            hasAccessibility = true,
            warning = WarningTypes.MediumWarning,
            isDeparted = true,
            id = UUID.randomUUID().toString(),
            detailsId = UUID.randomUUID().toString(),
            occupancyLevel = OccupancyLevel.MEDIUM,
            transportDurationInMinutes = 45,
            departure = Departure(
                time = JourneyTimes.Planned(
                    time = Date(),
//                    planned = Date(),
//                    estimated = Date().plusSec(3700),
                    isLiveTracking = true,
                ),
                departStopName = departStopName,
                departPlatform = departPlatform,
                directionName = "Backa",
                lineName = "1",
                colors = LegColors(
                    foreground = Color.White,
                    background = Color.Blue,
                    border = Color.White,
                ),
            ),
            originName = "Liseberg",
            destName = "Brunnsparken",
        )
    }

    fun lowWarnings() = listOf(
        Warning(
            severity = WarningSeverity.LOW,
            message = "Low warning",
        ),
        Warning(
            severity = WarningSeverity.LOW,
            message = "Low warning",
        ),
        Warning(
            severity = WarningSeverity.LOW,
            message = "Low warning",
        )
    )

    fun mediumWarnings() = listOf(
        Warning(
            severity = WarningSeverity.MEDIUM,
            message = "Medium warning",
        ),
        Warning(
            severity = WarningSeverity.MEDIUM,
            message = "Medium warning",
        ),
        Warning(
            severity = WarningSeverity.MEDIUM,
            message = "Medium warning",
        )
    )

    fun highWarnings() = listOf(
        Warning(
            severity = WarningSeverity.HIGH,
            message = "High warning",
        ),
        Warning(
            severity = WarningSeverity.HIGH,
            message = "High warning",
        ),
        Warning(
            severity = WarningSeverity.HIGH,
            message = "High warning",
        ),
    )

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
            id = "1",
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

    fun stopJourneyList(count: Int = 10): List<StopJourney> {
        val result = mutableListOf<StopJourney>()
        for (i in 0..count) {
            result.add(
                StopJourney(
                    time = JourneyTimes.Planned(
                        time = Date(),
                        isLiveTracking = true,
                    ),
                    direction = "Liseberg",
                    origin = "Brunnsparken",
                    colors = LegColors(
                        foreground = Color.White,
                        background = Color.Blue,
                        border = Color.White,
                    ),
                    transportMode = TransportMode.bus,
                    shortName = i.toString(),
                    hasAccessibility = true,
                    platform = "A",
                )
            )
        }
        return result.toList()
    }

    fun legStopList(count: Int = 10): List<LegStop> {
        val result = mutableListOf<LegStop>()
        for (i in 1..count) {
            result.add(
                LegStop(
                    id = i.toString(),
                    name = i.toString() +" " +legStopNames.random(),
                    time = legStopTimes.random(),
                    coordinate = null,
                    isPartOfLeg = true,
                    isLegStart = i == 1,
                    isLegEnd = i == count,
                )
            )
        }
        return result.toList()
    }

    fun busTracking(): VehiclePosition =
        VehiclePosition(
            name = "Test",
            position = Coordinate(0.0, 0.0),
            directionName = "Direction 1",
            colors = LegColors(
                foreground = Color.Blue,
                background = Color.White,
                border = Color.Blue,
            ),
            transportMode = TransportMode.bus,
        )

    private val legStopTimes = listOf("08:15", "08:22", "08:36", "08:42", "08:58", "09:15", "09:22", "09:36", "09:42", "09:58")
    private val legStopNames = listOf("Järntorget", "Brunnsparken", "Centralstationen", "Nordstan", "Hjalmar Brantingsplatsen", "Järntorget", "Brunnsparken", "Centralstationen", "Nordstan", "Hjalmar Brantingsplatsen")
}
