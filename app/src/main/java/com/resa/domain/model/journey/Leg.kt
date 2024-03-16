package com.resa.domain.model.journey

import com.resa.domain.model.Coordinate
import com.resa.domain.model.TransportMode

sealed class Leg {

    abstract val index: Int
    abstract val durationInMinutes: Int
    abstract val transportMode: TransportMode
    abstract val distanceInMeters: Int
    abstract val warnings: List<Warning>
    abstract val name: String

    data class Transport(
        override val index: Int,
        override val name: String,
        override val transportMode: TransportMode,
        override val durationInMinutes: Int,
        override val distanceInMeters: Int,
        override val warnings: List<Warning>,
        val arrivalLeg: ArrivalLeg = ArrivalLeg.None,
        val colors: LegColors,
        val departTime: JourneyTimes,
        val details: LegDetails,
    ) : Leg()

    data class AccessLink(
        override val index: Int,
        override val transportMode: TransportMode,
        override val durationInMinutes: Int,
        override val distanceInMeters: Int,
        override val name: String,
        override val warnings: List<Warning>,
        val departTime: JourneyTimes,
        val from: Coordinate?,
        val to: Coordinate?,
    ) : Leg()

    data class DepartureLink(
        override val index: Int = 0,
        override val transportMode: TransportMode,
        override val durationInMinutes: Int,
        override val name: String,
        override val distanceInMeters: Int,
        override val warnings: List<Warning>,
        val departTime: JourneyTimes,
        val from: Coordinate?,
        val to: Coordinate?,
    ) : Leg()

    data class ArrivalLink(
        override val index: Int,
        override val transportMode: TransportMode,
        override val durationInMinutes: Int,
        override val distanceInMeters: Int,
        override val name: String,
        override val warnings: List<Warning>,
        val destinationName: String,
        val departTime: JourneyTimes,
        val arriveTime: JourneyTimes,
        val from: Coordinate?,
        val to: Coordinate?,
    ) : Leg()

    fun getTime(): JourneyTimes {
        return when (this) {
            is AccessLink -> departTime
            is ArrivalLink -> arriveTime
            is DepartureLink -> departTime
            is Transport -> departTime
        }
    }
}
