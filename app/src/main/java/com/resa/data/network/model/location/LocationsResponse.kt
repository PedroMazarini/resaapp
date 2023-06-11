package com.resa.data.network.model.location

import com.resa.data.network.model.journeys.response.PaginationInfo
import com.resa.data.network.model.journeys.response.PaginationLinks
import com.squareup.moshi.JsonClass

/**
 * The response to a get locations request, includes the results and pagination information.
 *
 * @param results The results.
 * @param pagination
 * @param links
 */
@JsonClass(generateAdapter = true)
data class LocationsResponse(
    val results: List<Location>? = null,
    val pagination: PaginationInfo? = null,
    val links: PaginationLinks? = null,
)
