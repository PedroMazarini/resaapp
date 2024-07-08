package com.mazarini.resa.data.network.model.travelplanner.journeys

import com.mazarini.resa.data.network.model.travelplanner.journeys.response.Journey
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.PaginationInfo
import com.mazarini.resa.data.network.model.commonmodels.PaginationLinks
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
