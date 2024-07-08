package com.mazarini.resa.data.network.model.travelplanner.stopareas

import com.mazarini.resa.data.network.model.travelplanner.journeys.response.OccupancyInfo
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.ServiceJourney
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.StopPoint
import com.squareup.moshi.JsonClass

/**
 *
 *
 * @param stopPoint
 * @param plannedTime The planned time of the call in RFC 3339 format.
 * @param detailsReference A reference that should be used when getting detailed information about the journey.
 * @param serviceJourney
 * @param estimatedTime The estimated time of the call in RFC 3339 format.
 * @param estimatedOtherwisePlannedTime The best known time of the call in RFC 3339 format. Is EstimatedTime if exists, otherwise PlannedTime.
 * @param isCancelled Flag indicating if the departure or arrival is cancelled.
 * @param isPartCancelled Flag indicating if the departure or arrival is partially cancelled.
 * @param occupancy
 */
@JsonClass(generateAdapter = true)
data class StopDepartureOrArrival(
    val stopPoint: StopPoint,
    val plannedTime: String,
    val detailsReference: String? = null,
    val serviceJourney: ServiceJourney? = null,
    val estimatedTime: String? = null,
    val estimatedOtherwisePlannedTime: String? = null,
    val isCancelled: Boolean? = null,
    val isPartCancelled: Boolean? = null,
    val occupancy: OccupancyInfo? = null,
)
