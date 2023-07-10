package com.resa.ui.screens.locationsearch.model

import java.util.Date

data class JourneyFilters(
    val date: Date = Date(),
    val isDepartureFilters: Boolean = true,
)
