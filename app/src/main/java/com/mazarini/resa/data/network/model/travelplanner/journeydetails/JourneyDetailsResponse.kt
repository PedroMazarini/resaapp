package com.mazarini.resa.data.network.model.travelplanner.journeydetails

import com.mazarini.resa.data.network.model.travelplanner.journeys.response.ArrivalAccessLink
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.ConnectionLink
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.DepartureAccessLink
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.DestinationLink
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.OccupancyInfo
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.TariffZone
import com.mazarini.resa.data.network.model.travelplanner.ticketsuggestion.JourneyTicketSuggestions
import com.squareup.moshi.JsonClass

/**
 * Detailed information about a journey.
 *
 * @param departureAccessLink
 * @param tripLegs Detailed information, including stops, about the trip legs.
 * @param connectionLinks A list of ConnectionLinks between TripLegs, when applicable. The internal order of TripLegs and ConnectionLinks is defined by Index-property on the objects.
 * @param arrivalAccessLink
 * @param destinationLink
 * @param ticketSuggestionsResult
 * @param tariffZones The tariff zones that the journey traverses.
 * @param occupancy
 */
@JsonClass(generateAdapter = true)
data class JourneyDetailsResponse(
    val departureAccessLink: DepartureAccessLink? = null,
    val tripLegs: List<TripLegDetails>? = null,
    val connectionLinks: List<ConnectionLink>? = null,
    val arrivalAccessLink: ArrivalAccessLink? = null,
    val destinationLink: DestinationLink? = null,
    val ticketSuggestionsResult: JourneyTicketSuggestions? = null,
    val tariffZones: List<TariffZone>? = null,
    val occupancy: OccupancyInfo? = null,
)
