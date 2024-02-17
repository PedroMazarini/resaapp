package com.resa.data.network.model.travelplanner.journeys.response

import com.squareup.moshi.JsonClass

/**
 * @param gid The 16-digit Västtrafik gid of the stop area.
 * @param name The stop area name.
 * @param latitude The latitude of the stop point.
 * @param longitude The longitude of the stop point.
 * @param tariffZone1
 * @param tariffZone2
 */
@JsonClass(generateAdapter = true)
data class StopArea(
    val gid: String? = null,
    val name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val tariffZone1: TariffZone? = null,
    val tariffZone2: TariffZone? = null,
)
