package com.mazarini.resa.ui.screens.home.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.ui.model.JourneySearch
import com.mazarini.resa.ui.screens.home.model.SavedJourneyState
import com.mazarini.resa.ui.screens.home.model.StopPointsState

data class HomeUiState(
    val savedJourneyState: MutableState<SavedJourneyState> = mutableStateOf(SavedJourneyState.Loading),
    val recentJourneysState: MutableState<SavedJourneyState> = mutableStateOf(SavedJourneyState.Loading),
    val navigateToJourneySelection: MutableState<Boolean> = mutableStateOf(false),
    val requestGpsLocation: MutableState<Boolean> = mutableStateOf(false),
    val hasLocationPermission: MutableState<Boolean> = mutableStateOf(false),
    val hasCheckedPermission: MutableState<Boolean> = mutableStateOf(false),
    val journeySearchRequest: MutableState<JourneySearch?> = mutableStateOf(null),
    val stopPoints: MutableState<StopPointsState> = mutableStateOf(StopPointsState.Loading),
    val currentLocation: MutableState<Coordinate?> = mutableStateOf(null),
    val pinnedHomeJourney: MutableState<Journey?> = mutableStateOf(null),
    val pendingLocationUses: MutableState<HomeViewModel.PendingLocationUse> = mutableStateOf(
        HomeViewModel.PendingLocationUse.NONE
    ),
    val isDeparturesReloading: MutableState<Boolean> = mutableStateOf(false),
)