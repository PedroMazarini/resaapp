package com.mazarini.resa.data.network.model.travelplanner.journeys.response

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
 * @param shortName The short name of the line, usually 5 characters or less.
 * @param designation The designation of the line.
 * @param isWheelchairAccessible Flag indicating if the line is wheelchair accessible.
 */
@JsonClass(generateAdapter = true)
data class Line(
    val name: String? = null,
    val backgroundColor: String? = null,
    val foregroundColor: String? = null,
    val borderColor: String? = null,
    val transportMode: TransportMode? = null,
    val transportSubMode: TransportSubMode? = null,
    val shortName: String? = null,
    val designation: String? = null,
    val isWheelchairAccessible: Boolean? = null,
)
