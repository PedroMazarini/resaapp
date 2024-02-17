package com.resa.data.network.model.travelplanner.journeydetails

import com.resa.data.network.model.travelplanner.journeys.response.OccupancyInfo
import com.resa.data.network.model.travelplanner.journeys.response.StopPoint
import com.resa.data.network.model.travelplanner.journeys.response.TariffZone
import com.squareup.moshi.JsonClass

/**
 * Information about a call on the trip leg.
 *
 * @param stopPoint
 * @param plannedArrivalTime The planned arrival time for the call in RFC 3339 format.
 * @param plannedDepartureTime The planned departure time for the call in RFC 3339 format.
 * @param estimatedArrivalTime The estimated arrival time for the call in RFC 3339 format.
 * @param estimatedDepartureTime The estimated departure time for the call in RFC 3339 format.
 * @param estimatedOtherwisePlannedArrivalTime The best known time of the call in RFC 3339 format. Is EstimatedArrivalTime if exists, otherwise PlannedArrivalTime.
 * @param estimatedOtherwisePlannedDepartureTime The best known time of the call in RFC 3339 format. Is EstimatedDepartureTime if exists, otherwise PlannedDepartureTime.
 * @param plannedPlatform The planned platform of the call.
 * @param estimatedPlatform The estimated platform of the call.
 * @param latitude The latitude of the stop point of the call.
 * @param longitude The longitude of the stop point of the call.
 * @param index The index of the stop point of the call.
 * @param isOnTripLeg The call is on the trip leg.
 * @param isTripLegStart The call is the first on the trip leg.
 * @param isTripLegStop The call is the last on the trip leg.
 * @param tariffZones The primary tariff zone of the call. A call can be related to a stop area with multiple tariff zones  and this is the zone that for example should be displayed in overviews etc.
 * @param occupancy
 * @param isCancelled Flag indicating if the call is cancelled.
 * @param isDepartureCancelled Flag indicating if the departure is cancelled.
 * @param isArrivalCancelled Flag indicating if the arrival is cancelled.
 */
@JsonClass(generateAdapter = true)
data class CallDetails(
    val stopPoint: StopPoint,
    val plannedArrivalTime: String? = null,
    val plannedDepartureTime: String? = null,
    val estimatedArrivalTime: String? = null,
    val estimatedDepartureTime: String? = null,
    val estimatedOtherwisePlannedArrivalTime: String? = null,
    val estimatedOtherwisePlannedDepartureTime: String? = null,
    val plannedPlatform: String? = null,
    val estimatedPlatform: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val index: String? = null,
    val isOnTripLeg: Boolean? = null,
    val isTripLegStart: Boolean? = null,
    val isTripLegStop: Boolean? = null,
    val tariffZones: List<TariffZone>? = null,
    val occupancy: OccupancyInfo? = null,
    val isCancelled: Boolean? = null,
    val isDepartureCancelled: Boolean? = null,
    val isArrivalCancelled: Boolean? = null,
)
