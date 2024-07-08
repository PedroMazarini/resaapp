package com.mazarini.resa.data.network.model.geography.stopareas

import com.mazarini.resa.data.network.model.geography.stopareas.response.StopAreaSimplified
import com.mazarini.resa.data.network.model.commonmodels.PaginationLinks
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The response to a get locations request, includes the results and pagination information.
 *
 * @param stopAreas The results.
 * @param pagination
 * @param links
 */
@JsonClass(generateAdapter = true)
data class StopAreasSimplifiedResponse(
    val stopAreas: List<StopAreaSimplified>? = null,
    @Json(name = "properties")
    val pagination: PaginationInfo? = null,
    val links: PaginationLinks? = null,
)
