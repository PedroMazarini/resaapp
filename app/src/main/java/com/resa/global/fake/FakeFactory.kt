package com.resa.global.fake

import androidx.compose.ui.graphics.Color
import com.resa.domain.model.TransportMode
import com.resa.domain.model.journey.ArrivalLeg
import com.resa.domain.model.journey.Departure
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.Leg
import com.resa.domain.model.journey.LegColors
import com.resa.domain.model.journey.LegDetails
import com.resa.domain.model.journey.LegStop
import com.resa.domain.model.journey.OccupancyLevel
import com.resa.domain.model.journey.Warning
import com.resa.domain.model.journey.WarningSeverity
import com.resa.domain.model.journey.WarningTypes
import com.resa.domain.model.stoparea.StopJourney
import com.resa.domain.model.stoparea.StopPoint
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

    fun legList(count: Int = 10): List<Leg> {
        val result = mutableListOf<Leg>()
        for (i in 0..count) result.add(leg())
        return result.toList()
    }

    fun leg(
        index : Int = 0,
        name: String = "1",
        mode: TransportMode = TransportMode.walk,
        details: LegDetails = legDetails(),
        arrivalLeg: ArrivalLeg = ArrivalLeg.None,
        departTime: JourneyTimes = JourneyTimes.Planned(
            time = Date(),
            isLiveTracking = true,
        ),
        warnings: List<Warning> = listOf(),
    ): Leg.Transport {
        return Leg.Transport(
            index = index,
            name = name,
            arrivalLeg = arrivalLeg,
            transportMode = mode,
            durationInMinutes = 10,
            departTime = departTime,
            details = details,
            distanceInMeters = 720,
            colors = LegColors(
                foreground = Color.White,
                background = Color.Blue,
                border = Color.White,
            ),
            warnings = warnings,
        )
    }

    private fun legDetails(): LegDetails =
        LegDetails.Details(
            index = 1,
            pathWay = listOf(),
            legStops = legStopList(),
            platForm = null,
        )

    fun departWalkLeg(
        departTime: JourneyTimes = JourneyTimes.Planned(
            time = Date(),
            isLiveTracking = true,
        ),
        warnings: List<Warning> = listOf(),
    ): Leg.DepartureLink {
        return Leg.DepartureLink(
            index = 0,
            transportMode = TransportMode.walk,
            durationInMinutes = 10,
            name = "Liseberg",
            departTime = departTime,
            distanceInMeters = 720,
            from = null,
            to = null,
            warnings = warnings,
        )
    }

    fun accessWalkLeg(): Leg.AccessLink {
        return Leg.AccessLink(
            index = 0,
            transportMode = TransportMode.walk,
            durationInMinutes = 10,
            distanceInMeters = 720,
            name = "Liseberg",
            departTime = JourneyTimes.Planned(
                time = Date(),
                isLiveTracking = true,
            ),
            from = null,
            to = null,
            warnings = listOf(),
        )
    }

    fun arrivalWalkLeg(): Leg.ArrivalLink {
        return Leg.ArrivalLink(
            index = 0,
            transportMode = TransportMode.walk,
            durationInMinutes = 10,
            arriveTime = JourneyTimes.Planned(time = Date(), isLiveTracking = true),
            distanceInMeters = 720,
            name = "Smörkärnegatan 29",
            destinationName = "Liseberg",
            departTime = JourneyTimes.Planned(time = Date(), isLiveTracking = true),
            from = null,
            to = null,
            warnings = listOf(),
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
            detailsId = UUID.randomUUID().toString(),
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

    fun legStopList(count: Int = 10): List<LegStop> {
        val result = mutableListOf<LegStop>()
        for (i in 1..count) {
            result.add(
                LegStop(
                    id = i.toString(),
                    name = i.toString() +" " +legStopNames.random(),
                    time = legStopTimes.random(),
                )
            )
        }
        return result.toList()
    }

    private val legStopTimes = listOf("08:15", "08:22", "08:36", "08:42", "08:58", "09:15", "09:22", "09:36", "09:42", "09:58")
    private val legStopNames = listOf("Järntorget", "Brunnsparken", "Centralstationen", "Nordstan", "Hjalmar Brantingsplatsen", "Järntorget", "Brunnsparken", "Centralstationen", "Nordstan", "Hjalmar Brantingsplatsen")
}
