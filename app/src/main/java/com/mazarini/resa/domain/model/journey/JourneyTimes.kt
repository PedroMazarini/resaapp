package com.mazarini.resa.domain.model.journey

import com.mazarini.resa.domain.serializers.DateSerializer
import com.mazarini.resa.global.extensions.withinAMinute
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
sealed class JourneyTimes {

    abstract val isLiveTracking: Boolean

    @Serializable
    data class Planned(
        @Serializable(with = DateSerializer::class)
        val time: Date,
        override val isLiveTracking: Boolean,
    ) : JourneyTimes()

    @Serializable
    data class Changed(
        @Serializable(with = DateSerializer::class)
        val planned: Date,
        @Serializable(with = DateSerializer::class)
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

    fun arrival(): Date {
        return when (this) {
            is Planned -> time
            is Changed -> estimated
        }
    }
}
