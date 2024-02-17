package com.resa.ui.screens.journeyselection.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import com.resa.domain.model.journey.Journey
import com.resa.ui.screens.locationsearch.model.JourneyFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class JourneySelectionUiState(
    val upcomingJourneys: MutableState<Flow<PagingData<Journey>>> = mutableStateOf(emptyFlow()),
    val passedJourneys: MutableState<Flow<PagingData<Journey>>> = mutableStateOf(emptyFlow()),
    val journeyFilters: MutableState<JourneyFilters> = mutableStateOf(JourneyFilters()),
    val queryParams: MutableState<QueryJourneysParams> = mutableStateOf(QueryJourneysParams()),
    val filtersChanged: MutableState<Boolean> = mutableStateOf(false),
)

sealed class JourneysFeed {
    object Loading : JourneysFeed()
    data class Success(val journeys: List<Journey>) : JourneysFeed()
}
