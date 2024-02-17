package com.resa.data.network.model.travelplanner.journeys.response

import com.squareup.moshi.JsonClass

/**
 * @param type
 * @param severity
 * @param text
 */
@JsonClass(generateAdapter = true)
data class Note(
    val type: String? = null,
    val severity: Severity? = null,
    val text: String? = null,
)
