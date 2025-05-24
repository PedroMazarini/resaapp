package com.mazarini.resa.ui.screens.locationsearch.state

import kotlinx.coroutines.flow.emptyFlow
import androidx.paging.PagingData
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiState
import com.mazarini.resa.ui.model.Location
import kotlinx.coroutines.flow.Flow

const val MIN_LENGTH_FOR_SEARCH = 3

data class LocationSearchUiState(
    val originSearchRes: Flow<PagingData<Location>> = emptyFlow(),
    val destSearchRes: Flow<PagingData<Location>> = emptyFlow(),
    val originSearch: String = "",
    val destSearch: String = "",
    val requestOriginFocus: Boolean = false,
    val requestDestFocus: Boolean = false,
    val currentSearchType: CurrentSearchType = CurrentSearchType.ORIGIN,
    val originSelected: Location? = null,
    val destSelected: Location? = null,
    val isSelectionComplete: Boolean = false,
    val showLocationButton: Boolean = true,
    val currentLocationRequest: CurrentLocation? = null,
    /** Location suggestions */
    val savedLocations: List<Location> = emptyList(),
    val recentLocations: List<Location> = emptyList(),
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