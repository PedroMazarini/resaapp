package com.resa.data.network.model.journeys.response

import com.squareup.moshi.JsonClass

/**
 * Information about the coordinates
 *
 * @param latitude The latitude of this position (WGS84).
 * @param longitude The longitude of this position (WGS84).
 * @param elevation The elevation of this position (WGS84).
 * @param isOnTripLeg The coordinate is on the tripleg.
 * @param isTripLegStart The coordinate is on the first call of the leg.
 * @param isTripLegStop The coordinate is on the last call of the leg.
 */
@JsonClass(generateAdapter = true)
data class CoordinateInfo(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val elevation: Double? = null,
    val isOnTripLeg: Boolean? = null,
    val isTripLegStart: Boolean? = null,
    val isTripLegStop: Boolean? = null,
)
