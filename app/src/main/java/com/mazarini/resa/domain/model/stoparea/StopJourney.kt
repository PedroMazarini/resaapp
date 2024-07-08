package com.mazarini.resa.domain.model.stoparea

import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.domain.model.journey.LegColors

data class StopJourney(
    val time: JourneyTimes,
    val direction: String,
    val origin: String,
    val colors: LegColors,
    val platform: String,
    val transportMode: TransportMode,
    val shortName: String,
    val hasAccessibility: Boolean,
)
