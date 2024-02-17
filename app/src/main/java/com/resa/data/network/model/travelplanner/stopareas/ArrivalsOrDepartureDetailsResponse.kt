package com.resa.data.network.model.travelplanner.stopareas

import com.resa.data.network.model.travelplanner.journeydetails.JourneyDetailsServiceJourney
import com.resa.data.network.model.travelplanner.journeys.response.OccupancyInfo
import com.squareup.moshi.JsonClass

/**
 * @param serviceJourneys
 */
@JsonClass(generateAdapter = true)
data class ArrivalsOrDepartureDetailsResponse(
    val serviceJourneys: List<JourneyDetailsServiceJourney>? = null,
    val occupancy: OccupancyInfo? = null,
)
