package com.resa.data.network.model.stopareas

import com.resa.data.network.model.journeys.response.PaginationInfo
import com.resa.data.network.model.journeys.response.PaginationLinks
import com.squareup.moshi.JsonClass

/**
 * The response to a get arrivals request, includes the results and pagination information.
 *
 * @param results The results.
 * @param pagination
 * @param links
 */
@JsonClass(generateAdapter = true)
data class ArrivalsOrDeparturesResponse(
    val results: List<StopDepartureOrArrival>? = null,
    val pagination: PaginationInfo? = null,
    val links: PaginationLinks? = null,
)
