package com.mazarini.resa.ui.screens.departures.state

import com.mazarini.resa.domain.model.stoparea.StopJourney
import com.mazarini.resa.global.preferences.model.PreferredStop
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentLocation

data class DeparturesUiState(
    val isLoading: Boolean = false,
    val requestFocus: Boolean = false,
    val stopQuery: String = "",
    val stopQueryResult: List<Location> = emptyList(),
    val selectedStop: Location? = null,
    val recentLocations: List<Location> = listOf(),
    val preferredStop: PreferredStop? = null,
    val isPreferredSelected: Boolean = false,
    val departures: List<StopJourney> = emptyList(),
    val userLocationRequest: CurrentLocation? = null,
    val showStopQueryResult: Boolean = false,
)
