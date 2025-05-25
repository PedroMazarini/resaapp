package com.mazarini.resa.domain.model

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(
    val lat: Double,
    val lon: Double,
    val isPartOfTrip: Boolean = false,
    val isLegStart: Boolean = false,
    val isLegEnd: Boolean = false,
) {
    fun toLatLng() = LatLng(lat, lon)
}

fun List<Coordinate>.splitTripStops(): Triple<List<LatLng>, List<LatLng>, List<LatLng>> {
    var legStart = 0
    var legEnd = 0
    val tripCondition = object : (Int, Coordinate) -> Boolean {
        override fun invoke(index: Int, coordinate: Coordinate): Boolean {
            if (coordinate.isLegStart) legStart = index
            if (coordinate.isLegEnd) legEnd = index
            return coordinate.isPartOfTrip
        }
    }
    val priorCondition = object : (Int, Coordinate) -> Boolean {
        override fun invoke(index: Int, coordinate: Coordinate): Boolean {
            return index <= legStart
        }
    }
    val posteriorCondition = object : (Int, Coordinate) -> Boolean {
        override fun invoke(index: Int, coordinate: Coordinate): Boolean {
            return index >= legEnd
        }
    }

    val trip = this.filterIndexed { i, coordinate -> tripCondition(i, coordinate) }.map { it.toLatLng() }
    val prior = this.filterIndexed { i, coordinate -> priorCondition(i, coordinate) }.map { it.toLatLng() }
    val posterior = this.filterIndexed { i, coordinate -> posteriorCondition(i, coordinate) }.map { it.toLatLng() }

    return Triple(prior, trip, posterior)
}
fun LatLng.samePosition(other: Coordinate) = latitude == other.lat && longitude == other.lon
