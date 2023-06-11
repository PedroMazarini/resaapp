package com.resa.data.network.model.ticketsuggestion

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
