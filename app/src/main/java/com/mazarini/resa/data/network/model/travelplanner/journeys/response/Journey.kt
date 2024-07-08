package com.mazarini.resa.data.network.model.travelplanner.journeys.response

import com.squareup.moshi.JsonClass

/**
 * Information about a journey.
 *
 * @param reconstructionReference A reference that can be used to reconstruct this journey at a later time.
 * @param detailsReference A reference that should be used when getting detailed information about the journey.
 * @param departureAccessLink
 * @param tripLegs A list of trip legs on a journey, when applicable. A journey has either one or more trip legs or one  destination link.
 * @param connectionLinks A list of ConnectionLinks between TripLegs, when applicable. The internal order of TripLegs and ConnectionLinks is defined by Index-property on the objects.
 * @param arrivalAccessLink
 * @param destinationLink
 * @param isDeparted Flag indicating if the first trip leg of the journey is departed.
 * @param occupancy
 */
@JsonClass(generateAdapter = true)
data class Journey(
    /* A reference that can be used to reconstruct this journey at a later time. */
    val reconstructionReference: String? = null,
    /* A reference that should be used when getting detailed information about the journey. */
    val detailsReference: String? = null,
    val departureAccessLink: DepartureAccessLink? = null,
    /* A list of trip legs on a journey, when applicable. A journey has either one or more trip legs or one  destination link. */
    val tripLegs: List<TripLeg>? = null,
    /* A list of ConnectionLinks between TripLegs, when applicable. The internal order of TripLegs and ConnectionLinks is defined by Index-property on the objects. */
    val connectionLinks: List<ConnectionLink>? = null,
    val arrivalAccessLink: ArrivalAccessLink? = null,
    val destinationLink: DestinationLink? = null,
    /* Flag indicating if the first trip leg of the journey is departed. */
    val isDeparted: Boolean? = null,
    val occupancy: OccupancyInfo? = null,
)
