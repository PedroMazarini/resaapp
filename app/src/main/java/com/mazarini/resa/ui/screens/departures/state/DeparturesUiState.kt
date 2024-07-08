package com.mazarini.resa.ui.screens.departures.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mazarini.resa.domain.model.stoparea.StopJourney
import com.mazarini.resa.global.preferences.model.PreferredStop
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentLocation
import kotlinx.coroutines.flow.MutableStateFlow

data class DeparturesUiState(
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val requestFocus: MutableState<Boolean> = mutableStateOf(false),
    val stopQuery: MutableStateFlow<String> = MutableStateFlow(""),
    val stopQueryResult: MutableState<List<Location>> = mutableStateOf(emptyList()),
    val selectedStop: MutableStateFlow<Location?> = MutableStateFlow(null),
    val recentLocations: MutableState<List<Location>> = mutableStateOf(listOf()),
    val preferredStop: MutableStateFlow<PreferredStop?> = MutableStateFlow(null),
    val isPreferredSelected: MutableState<Boolean> = mutableStateOf(false),
    val departures: MutableState<List<StopJourney>> = mutableStateOf(emptyList()),
    val userLocationRequest: MutableState<CurrentLocation?> = mutableStateOf(null),
    val showStopQueryResult: MutableState<Boolean> = mutableStateOf(false),
)
