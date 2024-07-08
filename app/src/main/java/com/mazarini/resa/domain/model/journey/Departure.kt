package com.mazarini.resa.domain.model.journey

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Departure(
    val time: JourneyTimes,
    val lineName: String,
    val directionName: String,
    val departStopName: String,
    val departPlatform: String,
    val colors: LegColors,
) {
    fun isBefore(millis: Long): Boolean =
        when (time) {
            is JourneyTimes.Planned -> time.time.before(Date(millis))
            is JourneyTimes.Changed -> time.estimated.before(Date(millis))
        }

    fun sameMinute(millis: Long): Boolean =
        when (time) {
            is JourneyTimes.Planned -> (time.time.time - millis) in (-60000..0)
            is JourneyTimes.Changed -> (time.estimated.time - millis) in (-60000..0)
        }
}
