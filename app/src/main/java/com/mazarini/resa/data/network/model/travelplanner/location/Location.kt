package com.mazarini.resa.data.network.model.travelplanner.location

import com.mazarini.resa.data.network.model.travelplanner.journeys.response.LocationType
import com.squareup.moshi.JsonClass

/**
 * Information about a location.
 *
 * @param name The location name.
 * @param locationType
 * @param gid The 16-digit Västtrafik gid.
 * @param latitude The WGS84 latitude of the location.
 * @param longitude The WGS84 longitude of the location.
 * @param platform The location platform, only available for stop points.
 * @param straightLineDistanceInMeters The location straight line distance in meters.
 * @param hasLocalService Is \"Närtrafik\" (Local Service) available for the location?  Values are only available for LocationType: StopArea, PointOfInterest and Address.  Values are only available for endpoint: locations/by-text.
 */
@JsonClass(generateAdapter = true)
data class Location(
    val name: String,
    val locationType: LocationType,
    val gid: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val platform: String? = null,
    val straightLineDistanceInMeters: Int? = null,
    val hasLocalService: Boolean? = null,
)
