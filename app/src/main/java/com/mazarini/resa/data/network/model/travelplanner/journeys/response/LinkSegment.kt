package com.mazarini.resa.data.network.model.travelplanner.journeys.response

import com.squareup.moshi.JsonClass

/**
 * Represents a segment of a departure access link, arrival access link or destination link.
 *
 * @param name Segment name.
 * @param maneuver
 * @param orientation
 * @param maneuverDescription Description for the maneuver.
 * @param distanceInMeters Distance for this segment in meter.
 */
@JsonClass(generateAdapter = true)
data class LinkSegment(
    val name: String? = null,
    val maneuver: LinkSegmentManeuver? = null,
    val orientation: LinkSegmentOrientation? = null,
    val maneuverDescription: String? = null,
    val distanceInMeters: Int? = null,
)
