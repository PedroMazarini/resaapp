package com.resa.domain.repositoryAbstraction

import androidx.paging.PagingData
import com.resa.domain.model.JourneySearch
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.journey.JourneyResult
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface JourneysRepository {

    suspend fun queryJourneys(): Flow<PagingData<Journey>>
    suspend fun queryPassedJourneys(): Flow<PagingData<Journey>>

    suspend fun saveCurrentJourneyQuery(
        queryJourneysParams: QueryJourneysParams,
    )

    suspend fun getCurrentJourneyQuery(): Result<QueryJourneysParams>
    suspend fun saveJourneySearch(journeySearch: JourneySearch)
    suspend fun deleteSavedJourneySearch(id: Int)
    suspend fun getAllSavedJourneySearch(): Flow<List<JourneySearch>>
    suspend fun saveRecentJourneySearch(journeySearch: JourneySearch)
    suspend fun getAllRecentJourneySearch(): Flow<List<JourneySearch>>

    suspend fun fetchSelectedJourneyDetails()

    fun setSelectedJourney(journey: Journey)

    suspend fun getSelectedJourney(): StateFlow<Journey?>
}