package com.resa.domain.model.journey

import com.resa.global.extensions.withinAMinute
import java.util.Date

sealed class JourneyTimes {

    abstract val isLiveTracking: Boolean

    data class Planned(
        val time: Date,
        override val isLiveTracking: Boolean,
    ) : JourneyTimes()

    data class Changed(
        val planned: Date,
        val estimated: Date,
        override val isLiveTracking: Boolean,
    ) : JourneyTimes()

    fun hasDeparted(): Boolean {
        return when (this) {
            is Planned -> time.before(Date())
            is Changed -> estimated.before(Date())
        }
    }

    fun isWithinAMinute(): Boolean {
        return when (this) {
            is Planned -> time.withinAMinute(Date().time)
            is Changed -> estimated.withinAMinute(Date().time)
        }
    }
}
