package com.resa.domain.model.journey

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
}
