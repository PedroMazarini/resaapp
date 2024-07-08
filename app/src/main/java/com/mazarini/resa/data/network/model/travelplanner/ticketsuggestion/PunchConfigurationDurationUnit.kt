package com.mazarini.resa.data.network.model.travelplanner.ticketsuggestion

import com.squareup.moshi.JsonClass

/**
 * Unit of duration of validity of a single punch.
 *
 * Values: hours
 */
@JsonClass(generateAdapter = false)
enum class PunchConfigurationDurationUnit {
    hours,
}
