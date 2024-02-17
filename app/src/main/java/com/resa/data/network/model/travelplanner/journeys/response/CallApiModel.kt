package com.resa.data.network.model.travelplanner.journeys.response

import com.squareup.moshi.JsonClass

/**
 * Information about a call on the trip leg.
 *
 * @param stopPoint
 * @param plannedTime The planned time of the call in RFC 3339 format.
 * @param estimatedTime The estimated time of the call in RFC 3339 format.
 * @param estimatedOtherwisePlannedTime The best known time of the call in RFC 3339 format. Is EstimatedTime if exists, otherwise PlannedTime.
 * @param notes An ordered list (most important first) of notes related to the call.
 */
@JsonClass(generateAdapter = true)
data class CallApiModel(
    val stopPoint: StopPoint,
    val plannedTime: String,
    val estimatedTime: String? = null,
    val estimatedOtherwisePlannedTime: String? = null,
    val notes: List<Note>? = null,
)
