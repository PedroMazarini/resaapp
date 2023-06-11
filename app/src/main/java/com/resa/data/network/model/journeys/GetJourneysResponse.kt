package com.resa.data.network.model.journeys

import com.resa.data.network.model.journeys.response.Journey
import com.resa.data.network.model.journeys.response.PaginationInfo
import com.resa.data.network.model.journeys.response.PaginationLinks
import com.squareup.moshi.JsonClass

/**
 * @param results The results.
 * @param pagination
 * @param links
 */
@JsonClass(generateAdapter = true)
data class GetJourneysResponse(
    val results: List<Journey>? = null,
    val pagination: PaginationInfo? = null,
    val links: PaginationLinks? = null,
)
