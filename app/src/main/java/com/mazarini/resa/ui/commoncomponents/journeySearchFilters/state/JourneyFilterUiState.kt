package com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state

import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.ui.screens.locationsearch.model.JourneyFilters

data class JourneyFilterUiState(
    val filters: JourneyFilters = JourneyFilters(),
    val preferredModes: List<TransportMode> = emptyList(),
)
