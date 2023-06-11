package com.resa.data.network.model.journeys.response

import com.squareup.moshi.JsonClass

/**
 * Information about a service journey of a departure or arrival.
 *
 * @param gid 16-digit Västtrafik service journey gid.
 * @param direction A description of the direction.
 * @param number Västtrafik service journey number that the trip leg is a part of.
 * @param line
 */
@JsonClass(generateAdapter = true)
data class ServiceJourney(
    val gid: String,
    val direction: String? = null,
    val origin: String? = null,
    val number: String? = null,
    val line: Line? = null,
)
