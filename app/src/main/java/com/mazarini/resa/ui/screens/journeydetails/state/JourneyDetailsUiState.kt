package com.mazarini.resa.ui.screens.journeydetails.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.model.journey.LegStop
import kotlinx.coroutines.flow.MutableStateFlow

data class JourneyDetailsUiState(
    val legStopList: MutableState<List<LegStop>> = mutableStateOf(listOf()),
    val selectedJourney: MutableStateFlow<Journey?> = MutableStateFlow(null),
    val hasLocationAccess: MutableState<Boolean> = mutableStateOf(false),
    val isTrackingAvailable: MutableState<Boolean> = mutableStateOf(false),
    val shouldShowMap: MutableState<Boolean> = mutableStateOf(false),
    val followingVehicle: MutableState<VehiclePosition?> = mutableStateOf(null),
    val hasCheckedForLocationAccess: MutableState<Boolean> = mutableStateOf(false),
    val isCurrentJourneyAddedHome: MutableState<Boolean> = mutableStateOf(false),
    val trackedVehicles: MutableState<List<VehiclePosition>> = mutableStateOf(listOf()),
)
