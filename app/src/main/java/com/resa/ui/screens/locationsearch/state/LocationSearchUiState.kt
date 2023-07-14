package com.resa.ui.screens.locationsearch.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import com.resa.ui.model.Location
import com.resa.ui.screens.locationsearch.model.JourneyFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow

const val MIN_LENGTH_FOR_SEARCH = 3

data class LocationSearchUiState(
    val originSearchRes: MutableState<Flow<PagingData<Location>>> = mutableStateOf(emptyFlow()),
    val destSearchRes: MutableState<Flow<PagingData<Location>>> = mutableStateOf(emptyFlow()),
    val originSearch: MutableStateFlow<String> = MutableStateFlow(""),
    val destSearch: MutableStateFlow<String> = MutableStateFlow(""),
    val requestOriginFocus: MutableState<Boolean> = mutableStateOf(false),
    val requestDestFocus: MutableState<Boolean> = mutableStateOf(false),
    val currentSearchType: MutableStateFlow<CurrentSearchType> = MutableStateFlow(CurrentSearchType.ORIGIN),
    val journeyFilters: MutableState<JourneyFilters> = mutableStateOf(JourneyFilters()),
    val originSelected: MutableStateFlow<Location?> = MutableStateFlow(null),
    val destSelected: MutableStateFlow<Location?> = MutableStateFlow(null),
    val isSelectionComplete: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val showLocationButton: MutableState<Boolean> = mutableStateOf(true),
    val currentLocationRequest: MutableState<CurrentLocation?> = mutableStateOf(null),
    /** Location suggestions */
    val savedLocations: MutableState<List<Location>> = mutableStateOf(emptyList()),
    val recentLocations: MutableState<List<Location>> = mutableStateOf(emptyList()),
)

enum class CurrentSearchType {
    ORIGIN,
    DESTINATION,
}

sealed class CurrentLocation {
    object Request : CurrentLocation()
    object Loading : CurrentLocation()
}