package com.resa.domain.model.journey

import com.resa.global.extensions.hasPassed
import java.util.Date

data class Departure(
    val time: JourneyTimes,
    val departStopName: String,
    val departPlatform: String,
) {
    fun isBefore(millis: Long): Boolean =
        when (time) {
            is JourneyTimes.Planned -> time.time.before(Date(millis))
            is JourneyTimes.Changed -> time.estimated.before(Date(millis))
        }
}
