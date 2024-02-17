package com.resa.ui.screens.home.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.resa.domain.model.Coordinate
import com.resa.ui.model.JourneySearch
import com.resa.ui.screens.home.model.SavedJourneyState
import com.resa.ui.screens.home.model.StopPointsState
import kotlinx.coroutines.flow.MutableStateFlow

data class HomeUiState(
    var savedJourneyState: MutableState<SavedJourneyState> = mutableStateOf(SavedJourneyState.Loading),
    var navigateToJourneySelection: MutableState<Boolean> = mutableStateOf(false),
    var requestGpsLocation: MutableState<Boolean> = mutableStateOf(false),
    var hasLocationPermission: MutableState<Boolean> = mutableStateOf(false),
    var hasCheckedPermission: MutableState<Boolean> = mutableStateOf(false),
    var journeySearchRequest: MutableState<JourneySearch?> = mutableStateOf(null),
    var stopPoints: MutableStateFlow<StopPointsState> = MutableStateFlow(StopPointsState.Loading),
    var currentLocation: MutableState<Coordinate?> = mutableStateOf(null),
    var pendingLocationUses: MutableState<HomeViewModel.PendingLocationUse> = mutableStateOf(HomeViewModel.PendingLocationUse.NONE),
    var isDeparturesReloading: MutableState<Boolean> = mutableStateOf(false),
)