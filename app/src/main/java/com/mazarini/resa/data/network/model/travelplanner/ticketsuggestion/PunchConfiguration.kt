package com.mazarini.resa.data.network.model.travelplanner.ticketsuggestion

import com.squareup.moshi.JsonClass

/**
 * Represents punch configuration.
 *
 * @param quota Punch quota of a single ticket.
 * @param duration
 */
@JsonClass(generateAdapter = true)
data class PunchConfiguration(
    val quota: Int? = null,
    val duration: PunchConfigurationDuration? = null,
)
