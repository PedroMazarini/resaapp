package com.resa.data.network.model.journeydetails

import com.resa.data.network.model.journeys.response.CoordinateInfo
import com.resa.data.network.model.journeys.response.Line
import com.squareup.moshi.JsonClass

/**
 * Information about a service journey.
 *
 * @param gid 16-digit Västtrafik service journey gid that the trip leg is a part of.
 * @param direction A description of the direction.
 * @param line
 * @param serviceJourneyCoordinates The coordinates on the service journey.
 * @param callsOnServiceJourney All calls on the service journey.
 */
@JsonClass(generateAdapter = true)
data class JourneyDetailsServiceJourney(
    val gid: String? = null,
    val direction: String? = null,
    val line: Line? = null,
    val serviceJourneyCoordinates: List<CoordinateInfo>? = null,
    val callsOnServiceJourney: List<CallDetails>? = null,
)
