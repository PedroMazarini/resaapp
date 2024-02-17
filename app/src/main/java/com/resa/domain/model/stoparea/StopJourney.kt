package com.resa.domain.model.stoparea

import com.resa.domain.model.TransportMode
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.LegColors

data class StopJourney(
    val time: JourneyTimes,
    val isCancelled: Boolean,
    val isPartCancelled: Boolean,
    val direction: String,
    val origin: String,
    val number: String,
    val colors: LegColors,
    val transportMode: TransportMode,
    val shortName: String,
    val hasAccessibility: Boolean,
)
