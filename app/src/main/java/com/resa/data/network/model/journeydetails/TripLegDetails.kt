package com.resa.data.network.model.journeydetails

import com.resa.data.network.model.journeys.response.CoordinateInfo
import com.resa.data.network.model.journeys.response.OccupancyInfo
import com.resa.data.network.model.journeys.response.TariffZone
import com.squareup.moshi.JsonClass

/**
 * Detailed information about a Public Transport trip leg.
 *
 * @param serviceJourneys The service journey for the trip leg.
 * @param callsOnTripLeg The calls on the trip leg.
 * @param tripLegCoordinates The coordinates for the trip leg.
 * @param tariffZones The tariff zones that the trip leg traverses.
 * @param isCancelled Flag indicating if the trip leg is cancelled.
 * @param isPartCancelled Flag indicating if the trip leg is partially cancelled.
 * @param occupancy 
 * @param journeyLegIndex Index of Leg in Journey
 */
@JsonClass(generateAdapter = true)
data class TripLegDetails (
    val serviceJourneys: List<JourneyDetailsServiceJourney>? = null,
    val callsOnTripLeg: List<CallDetails>? = null,
    val tripLegCoordinates: List<CoordinateInfo>? = null,
    val tariffZones: List<TariffZone>? = null,
    val isCancelled: Boolean? = null,
    val isPartCancelled: Boolean? = null,
    val occupancy: OccupancyInfo? = null,
    val journeyLegIndex: Int? = null,
)
