package com.resa.data.network.model.travelplanner.ticketsuggestion

import com.squareup.moshi.JsonClass


/**
 *
 *
 * Values: unknown,minutes,hours,days,year,semester,schoolyear,unlimited
 */
@JsonClass(generateAdapter = false)
enum class TimeValidityUnit {
    unknown,
    minutes,
    hours,
    days,
    year,
    semester,
    schoolyear,
    unlimited,
}
