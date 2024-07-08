package com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.ui.screens.locationsearch.model.JourneyFilters

data class JourneyFilterUiState(
    val filters: MutableState<JourneyFilters> = mutableStateOf(JourneyFilters()),
    val preferredModes: MutableState<List<TransportMode>> = mutableStateOf(emptyList()),
)
