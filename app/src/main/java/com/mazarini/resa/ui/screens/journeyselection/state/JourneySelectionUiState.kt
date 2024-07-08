package com.mazarini.resa.ui.screens.journeyselection.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiState
import com.mazarini.resa.ui.screens.locationsearch.model.JourneyFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class JourneySelectionUiState(
    val upcomingJourneys: MutableState<Flow<PagingData<Journey>>> = mutableStateOf(emptyFlow()),
    val passedJourneys: MutableState<Flow<PagingData<Journey>>> = mutableStateOf(emptyFlow()),
    val queryParams: MutableState<QueryJourneysParams> = mutableStateOf(QueryJourneysParams()),
    val filtersChanged: MutableState<Boolean> = mutableStateOf(false),

    val filterUiState: JourneyFilterUiState = JourneyFilterUiState(
        filters = mutableStateOf(JourneyFilters()),
        preferredModes = mutableStateOf(emptyList()),
    ),
)

sealed class JourneysFeed {
    object Loading : JourneysFeed()
    data class Success(val journeys: List<Journey>) : JourneysFeed()
}
