package com.mazarini.resa.data.network.model.travelplanner.journeys.response

import com.squareup.moshi.JsonClass

/**
 * Pagination information.
 *
 * @param limit The requested number of results.
 * @param offset The requested offset in the results array.
 * @param propertySize The actual number of returned results.
 */
@JsonClass(generateAdapter = true)
data class PaginationInfo(
    val limit: Int? = null,
    val offset: Int? = null,
    val propertySize: Int? = null,
)
