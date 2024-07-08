package com.mazarini.resa.data.network.model.geography.stoppoints

import com.mazarini.resa.data.network.model.commonmodels.PaginationLinks
import com.mazarini.resa.data.network.model.geography.stopareas.PaginationInfo
import com.mazarini.resa.data.network.model.geography.stopareas.response.StopPointSimplified
import com.squareup.moshi.Json

/**
 * 
 *
 * @param links 
 * @param properties 
 * @param stopPoints 
 */

data class StopPointsResponse (
    val links: PaginationLinks? = null,
    @Json(name = "properties")
    val pagination: PaginationInfo? = null,
    val stopPoints: List<StopPointSimplified>? = null
)
