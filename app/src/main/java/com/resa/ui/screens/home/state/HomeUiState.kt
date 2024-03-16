package com.resa.ui.screens.home.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.resa.domain.model.Coordinate
import com.resa.ui.model.JourneySearch
import com.resa.ui.screens.home.model.SavedJourneyState
import com.resa.ui.screens.home.model.StopPointsState
import kotlinx.coroutines.flow.MutableStateFlow

data class HomeUiState(
    val savedJourneyState: MutableState<SavedJourneyState> = mutableStateOf(SavedJourneyState.Loading),
    val navigateToJourneySelection: MutableState<Boolean> = mutableStateOf(false),
    val requestGpsLocation: MutableState<Boolean> = mutableStateOf(false),
    val hasLocationPermission: MutableState<Boolean> = mutableStateOf(false),
    val hasCheckedPermission: MutableState<Boolean> = mutableStateOf(false),
    val journeySearchRequest: MutableState<JourneySearch?> = mutableStateOf(null),
    val stopPoints: MutableState<StopPointsState> = mutableStateOf(StopPointsState.Loading),
    val currentLocation: MutableState<Coordinate?> = mutableStateOf(null),
    val pendingLocationUses: MutableState<HomeViewModel.PendingLocationUse> = mutableStateOf(HomeViewModel.PendingLocationUse.NONE),
    val isDeparturesReloading: MutableState<Boolean> = mutableStateOf(false),
)