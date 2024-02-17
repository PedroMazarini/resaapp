package com.resa.data.network.model.commonmodels

import com.squareup.moshi.JsonClass

/**
 * Pagination navigation links.
 *
 * @param previous Link to the previous results page, if available, otherwise null.
 * @param next Link to the next results page, if available, otherwise null. Not guaranteed to give a result if called.
 * @param current Link to the current results page, if available, otherwise null. Not guaranteed to give a result if called.
 */
@JsonClass(generateAdapter = true)
data class PaginationLinks(
    val previous: String? = null,
    val next: String? = null,
    val current: String? = null,
)
