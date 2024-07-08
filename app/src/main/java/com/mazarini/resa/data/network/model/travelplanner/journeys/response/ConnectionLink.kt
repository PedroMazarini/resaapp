package com.mazarini.resa.data.network.model.travelplanner.journeys.response

import com.squareup.moshi.JsonClass

/**
 * Information about a walk, bike or car link between two public transport trip legs.
 *
 * @param transportMode
 * @param transportSubMode
 * @param origin
 * @param destination
 * @param notes An ordered list (most important first) of notes related to the access link.
 * @param distanceInMeters Distance in meters.
 * @param plannedDepartureTime The planned departure time in RFC 3339 format.
 * @param plannedArrivalTime The planned arrival time in RFC 3339 format.
 * @param plannedDurationInMinutes The planned duration in minutes.
 * @param estimatedDepartureTime The estimated departure time in RFC 3339 format, if available.
 * @param estimatedArrivalTime The estimated arrival time in RFC 3339 format, if available.
 * @param estimatedDurationInMinutes The estimated duration in minutes, if available.
 * @param estimatedNumberOfSteps Number of steps based on the distance and an estimated step length of 0.65 meters.
 * @param linkCoordinates The coordinates for the link.
 * @param segments The segments that make up this link.
 * @param journeyLegIndex Index of Leg in Journey
 */
@JsonClass(generateAdapter = true)
data class ConnectionLink(
    val transportMode: TransportMode? = null,
    val transportSubMode: TransportSubMode? = null,
    val origin: CallApiModel? = null,
    val destination: CallApiModel? = null,
    val notes: List<Note>? = null,
    val distanceInMeters: Int? = null,
    val plannedDepartureTime: String? = null,
    val plannedArrivalTime: String? = null,
    val plannedDurationInMinutes: Int? = null,
    val estimatedDepartureTime: String? = null,
    val estimatedArrivalTime: String? = null,
    val estimatedDurationInMinutes: Int? = null,
    val estimatedNumberOfSteps: Int? = null,
    val linkCoordinates: List<CoordinateInfo>? = null,
    val segments: List<LinkSegment>? = null,
    val journeyLegIndex: Int? = null,
)
