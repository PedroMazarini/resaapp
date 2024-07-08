package com.mazarini.resa.domain.model.journey

import java.util.Date

sealed class LegPlatforms {

    data class PlannedOnly(
        val planned: String,
    ) : LegPlatforms()

    data class RealTime(
        val planned: Date,
        val estimated: Date,
    ) : LegPlatforms()
}
