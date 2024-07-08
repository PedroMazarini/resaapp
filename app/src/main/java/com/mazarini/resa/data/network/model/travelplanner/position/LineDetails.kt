package com.mazarini.resa.data.network.model.travelplanner.position

import com.mazarini.resa.data.network.model.travelplanner.journeys.response.TransportMode
import com.mazarini.resa.data.network.model.travelplanner.journeys.response.TransportSubMode
import com.squareup.moshi.JsonClass

/**
 * Information about a line.
 *
 * @param name The line name.
 * @param backgroundColor The background color of the line symbol.
 * @param foregroundColor The foreground color of the line symbol.
 * @param borderColor The border color of the line symbol.
 * @param transportMode
 * @param transportSubMode
 */
@JsonClass(generateAdapter = true)
data class LineDetails(
    val name: String? = null,
    val backgroundColor: String? = null,
    val foregroundColor: String? = null,
    val borderColor: String? = null,
    val transportMode: TransportMode? = null,
    val transportSubMode: TransportSubMode? = null,
)
