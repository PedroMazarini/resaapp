package com.resa.data.network.model.journeys.response

import com.squareup.moshi.JsonClass

/**
 * Contains information about occupancy.
 *
 * @param level
 * @param source
 */
@JsonClass(generateAdapter = true)
data class OccupancyInfo(
    val level: OccupancyLevel? = null,
    val source: OccupancySource? = null,
)
