package com.resa.data.network.model.travelplanner.journeys.response

import com.squareup.moshi.JsonClass

/**
 * @param gid The 16-digit Västtrafik gid of the stop point.
 * @param name The stop point name.
 * @param platform The platform of the stop point.
 * @param latitude The latitude coordinate of the stop point.
 * @param longitude The longitude coordinate of the stop point.
 * @param stopArea
 */
@JsonClass(generateAdapter = true)
data class StopPoint(
    val gid: String,
    val name: String,
    val platform: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val stopArea: StopArea? = null,
)
