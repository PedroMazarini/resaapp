package com.resa.domain.model.journey

import com.resa.global.extensions.hasPassed

data class Departure(
    val time: JourneyTimes,
    val departStopName: String,
    val departPlatform: String,
) {
    fun hasPassed(): Boolean =
        when (time) {
            is JourneyTimes.Planned -> time.time.hasPassed()
            is JourneyTimes.Changed -> time.estimated.hasPassed()
        }

}
