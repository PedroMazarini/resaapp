package com.resa.data.network.model.ticketsuggestion

import com.squareup.moshi.JsonClass

/**
 * Describes the duration of validity of a single punch.
 *
 * @param unit
 * @param amount Duration of validity of a single punch.
 */
@JsonClass(generateAdapter = true)
data class PunchConfigurationDuration(
    val unit: PunchConfigurationDurationUnit? = null,
    val amount: Int? = null,
)
