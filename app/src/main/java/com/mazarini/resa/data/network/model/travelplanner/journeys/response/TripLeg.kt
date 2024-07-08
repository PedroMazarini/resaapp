package com.mazarini.resa.data.network.model.travelplanner.journeys.response

import com.squareup.moshi.JsonClass

/**
 * Information about a journey trip leg.
 *
 * @param origin
 * @param destination
 * @param isCancelled Flag indicating if the trip leg is cancelled.
 * @param isPartCancelled Flag indicating if the trip leg is partially cancelled.
 * @param serviceJourney
 * @param notes An ordered list (most important first) of notes related to the trip leg.
 * @param estimatedDistanceInMeters Estimated distance in meters. Only for transport mode Walk.
 * @param plannedConnectingTimeInMinutes The planned (according to timetable) connecting time in minutes relative to  the previous public transport trip leg (if any).
 * @param estimatedConnectingTimeInMinutes The estimated (according to real-time data) connecting time in minutes relative to  the previous public transport trip leg (if any).
 * @param isRiskOfMissingConnection Flag indicating that there is less than 5 minutes margin between arriving at the  origin stop point and the departure from that stop point.
 * @param plannedDepartureTime The planned departure time in RFC 3339 format.
 * @param plannedArrivalTime The planned arrival time in RFC 3339 format.
 * @param plannedDurationInMinutes The planned duration in minutes.
 * @param estimatedDepartureTime The estimated departure time in RFC 3339 format, if available.
 * @param estimatedArrivalTime The estimated arrival time in RFC 3339 format, if available.
 * @param estimatedDurationInMinutes The estimated duration in minutes, if available.
 * @param estimatedOtherwisePlannedArrivalTime The best known time of the arrival in RFC 3339 format. Is EstimatedArrivalTime if exists, otherwise PlannedArrivalTime.
 * @param estimatedOtherwisePlannedDepartureTime The best known time of the departure in RFC 3339 format. Is EstimatedDepartureTime if exists, otherwise PlannedDepartureTime.
 * @param occupancy
 * @param journeyLegIndex Index of Leg in Journey
 */
@JsonClass(generateAdapter = true)
data class TripLeg(
    val origin: CallApiModel,
    val destination: CallApiModel,
    val isCancelled: Boolean,
    val isPartCancelled: Boolean? = null,
    val serviceJourney: ServiceJourney? = null,
    val notes: List<Note>? = null,
    val estimatedDistanceInMeters: Int? = null,
    val plannedConnectingTimeInMinutes: Int? = null,
    val estimatedConnectingTimeInMinutes: Int? = null,
    val isRiskOfMissingConnection: Boolean? = null,
    val plannedDepartureTime: String? = null,
    val plannedArrivalTime: String? = null,
    val plannedDurationInMinutes: Int? = null,
    val estimatedDepartureTime: String? = null,
    val estimatedArrivalTime: String? = null,
    val estimatedDurationInMinutes: Int? = null,
    val estimatedOtherwisePlannedArrivalTime: String? = null,
    val estimatedOtherwisePlannedDepartureTime: String? = null,
    val occupancy: OccupancyInfo? = null,
    val journeyLegIndex: Int? = null,
)
