package com.mazarini.resa.data.network.model.travelplanner.position

import com.mazarini.resa.data.network.model.travelplanner.journeys.response.Note
import com.squareup.moshi.JsonClass

/**
 * @param detailsReference Journey reference
 * @param line
 * @param notes Journey notes
 * @param name Journey name
 * @param direction Journey direction
 * @param latitude Current latitude of journey
 * @param longitude Current longitude of journey
 */
@JsonClass(generateAdapter = true)
data class Position(
    val detailsReference: String? = null,
    val line: LineDetails? = null,
    val notes: List<Note>? = null,
    val name: String? = null,
    val direction: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
