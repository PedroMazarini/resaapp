package com.mazarini.resa.data.network.model.travelplanner.journeys.response

import com.squareup.moshi.JsonClass

/**
 * Information about an endpoint on an access link.
 *
 * @param name The location name.
 * @param locationType
 * @param plannedTime The planned time in RFC 3339 format.
 * @param gid The 16-digit Västtrafik gid.
 * @param latitude The WGS84 latitude of the location.
 * @param longitude The WGS84 longitude of the location.
 * @param estimatedTime The estimated time in RFC 3339 format.
 * @param estimatedOtherwisePlannedTime The best known time of the link in RFC 3339 format. Is EstimatedTime if exists, otherwise PlannedTime.
 * @param notes An ordered list (most important first) of notes related to the end point.
 */
@JsonClass(generateAdapter = true)
data class LinkEndpoint(
    val name: String,
    val locationType: LocationType,
    val plannedTime: String,
    val gid: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val estimatedTime: String? = null,
    val estimatedOtherwisePlannedTime: String? = null,
    val notes: List<Note>? = null,
)
