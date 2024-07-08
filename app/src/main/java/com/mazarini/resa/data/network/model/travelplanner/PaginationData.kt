package com.mazarini.resa.data.network.model.travelplanner

/**
 * Pagination information.
 *
 * @param limit The requested number of results.
 * @param offset The requested offset in the results array.
 * @param propertySize The actual number of returned results.
 * @param previous Link to the previous results page, if available, otherwise null.
 * @param next Link to the next results page, if available, otherwise null. Not guaranteed to give a result if called.
 * @param current Link to the current results page, if available, otherwise null. Not guaranteed to give a result if called.
 */
data class PaginationData(
    val limit: Int,
    val offset: Int,
    val propertySize: Int,
    val previous: String,
    val next: String,
    val current: String,
)