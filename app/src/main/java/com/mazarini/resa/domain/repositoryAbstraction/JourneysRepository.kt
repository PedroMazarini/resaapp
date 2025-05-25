package com.mazarini.resa.domain.repositoryAbstraction

import androidx.paging.PagingData
import com.mazarini.resa.domain.model.JourneySearch
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface JourneysRepository {

    suspend fun queryJourneys(): Flow<PagingData<Journey>>
    suspend fun queryPassedJourneys(): Flow<PagingData<Journey>>

    suspend fun listenVehiclePosition(): Flow<List<VehiclePosition>>

    suspend fun checkHasVehiclePosition(): Result<Boolean>
    suspend fun startVehicleTracking()

    suspend fun saveCurrentJourneyQuery(
        queryJourneysParams: QueryJourneysParams,
    )

    suspend fun getCurrentJourneyQuery(): Result<QueryJourneysParams>
    suspend fun saveJourneySearch(journeySearch: JourneySearch)
    suspend fun deleteSavedJourneySearch(id: String)
    suspend fun deleteRecentJourneySearch(id: String)
    suspend fun getAllSavedJourneySearch(): Flow<List<JourneySearch>>
    suspend fun saveRecentJourneySearch(journeySearch: JourneySearch)
    suspend fun getAllRecentJourneySearch(): Flow<List<JourneySearch>>

    suspend fun saveJourneyToHome()
    suspend fun loadSavedJourneyToHome()
    suspend fun fetchSelectedJourneyDetails()

    fun setSelectedJourney(journey: Journey)

    suspend fun getSelectedJourney(): StateFlow<Journey?>

    suspend fun clearOldJourneySearches()
}