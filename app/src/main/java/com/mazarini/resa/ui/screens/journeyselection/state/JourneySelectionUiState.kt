package com.mazarini.resa.ui.screens.journeyselection.state

import androidx.paging.PagingData
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class JourneySelectionUiState(
    val upcomingJourneys: Flow<PagingData<Journey>> = emptyFlow(),
    val passedJourneys: Flow<PagingData<Journey>> = emptyFlow(),
    val queryParams: QueryJourneysParams = QueryJourneysParams(),
    val filtersChanged: Boolean = false,
    val showFeatureHighlight: Boolean = false,
    val filterUiState: JourneyFilterUiState = JourneyFilterUiState(),
)
