package com.mazarini.resa.ui.screens.locationsearch.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiState
import com.mazarini.resa.ui.model.Location
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
//    val journeyFilters: MutableState<JourneyFilters> = mutableStateOf(JourneyFilters()),
    val originSelected: MutableStateFlow<Location?> = MutableStateFlow(null),
    val destSelected: MutableStateFlow<Location?> = MutableStateFlow(null),
    val isSelectionComplete: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val showLocationButton: MutableState<Boolean> = mutableStateOf(true),
    val currentLocationRequest: MutableState<CurrentLocation?> = mutableStateOf(null),
    /** Location suggestions */
    val savedLocations: MutableState<List<Location>> = mutableStateOf(emptyList()),
    val recentLocations: MutableState<List<Location>> = mutableStateOf(emptyList()),

    /** Filters UI state */
    val filtersUiState: JourneyFilterUiState = JourneyFilterUiState(),
)

enum class CurrentSearchType {
    ORIGIN,
    DESTINATION,
}

sealed class CurrentLocation {
    object Request : CurrentLocation()
    object Loading : CurrentLocation()
}