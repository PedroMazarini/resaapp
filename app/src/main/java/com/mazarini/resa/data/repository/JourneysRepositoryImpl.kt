package com.mazarini.resa.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.mazarini.resa.data.cache.service.RecentJourneySearchCacheService
import com.mazarini.resa.data.cache.service.SavedJourneySearchCacheService
import com.mazarini.resa.data.network.datasource.abstraction.JourneysDatasource
import com.mazarini.resa.domain.model.JourneySearch
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.model.journey.LegDetails
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import com.mazarini.resa.global.JsonEncoder
import com.mazarini.resa.global.analytics.loge
import com.mazarini.resa.global.preferences.PrefsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JourneysRepositoryImpl
@Inject
constructor(
    private val journeysDatasource: JourneysDatasource,
    private val prefsProvider: PrefsProvider,
    private val savedJourneySearchService: SavedJourneySearchCacheService,
    private val recentJourneySearchService: RecentJourneySearchCacheService,
) : JourneysRepository {

    private val selectedJourney: MutableStateFlow<Journey?> = MutableStateFlow(null)

    private val trackedVehicle: MutableSharedFlow<List<VehiclePosition>> = MutableSharedFlow()

    @WorkerThread
    override suspend fun queryJourneys(): Flow<PagingData<Journey>> =
        journeysDatasource.queryJourneys(
            journeysParams = getCurrentJourneysQuery(),
            token = getToken(),
        ).flowOn(Dispatchers.IO)

    @WorkerThread
    override suspend fun queryPassedJourneys(): Flow<PagingData<Journey>> =
        journeysDatasource.queryPassedJourneys(
            journeysParams = getCurrentJourneysQuery(),
            token = getToken(),
        ).flowOn(Dispatchers.IO)

    override suspend fun listenVehiclePosition(): Flow<List<VehiclePosition>> {
        return trackedVehicle
    }

    override suspend fun saveCurrentJourneyQuery(queryJourneysParams: QueryJourneysParams) {
        prefsProvider.setCurrentJourneysQuery(JsonEncoder.encode(queryJourneysParams).orEmpty())
    }

    override suspend fun getCurrentJourneyQuery(): Result<QueryJourneysParams> {
        return prefsProvider.getCurrentJourneysQuery()?.let {
            Result.success(
                JsonEncoder.decode<QueryJourneysParams>(it)
            )
        } ?: return Result.failure(Throwable("No current journey query found"))
    }

    private suspend fun getToken() = prefsProvider.getToken().token

    private suspend fun getCurrentJourneysQuery() = JsonEncoder.decode<QueryJourneysParams>(
        prefsProvider.getCurrentJourneysQuery().orEmpty()
    )

    override suspend fun saveJourneySearch(journeySearch: JourneySearch) {
        savedJourneySearchService.saveJourneySearch(journeySearch)
    }

    override suspend fun deleteSavedJourneySearch(id: String) {
        savedJourneySearchService.deleteJourneySearch(id)
    }

    override suspend fun deleteRecentJourneySearch(id: String) {
        recentJourneySearchService.deleteJourneySearch(id)
    }

    override suspend fun getAllSavedJourneySearch(): Flow<List<JourneySearch>> =
        savedJourneySearchService.getAllJourneySearches()

    override suspend fun saveRecentJourneySearch(journeySearch: JourneySearch) {
        recentJourneySearchService.saveJourneySearch(journeySearch)
    }

    override suspend fun saveJourneyToHome() {
        prefsProvider.setSavedJourney(selectedJourney.value)
    }

    override suspend fun loadSavedJourneyToHome() {
        selectedJourney.value = prefsProvider.getSavedJourney()
    }

    override suspend fun clearOldJourneySearches() {
        recentJourneySearchService.clearOldJourneySearch()
    }

    override suspend fun getAllRecentJourneySearch(): Flow<List<JourneySearch>> =
        recentJourneySearchService.getAllJourneySearches()

    override suspend fun fetchSelectedJourneyDetails() {
        selectedJourney.value?.let {
            journeysDatasource.getJourneyDetails(
                detailsRef = it.detailsId,
                token = getToken(),
            ).onSuccess {
                updateSelectedJourney(it)
            }
        }
    }

    override fun setSelectedJourney(journey: Journey) {
        selectedJourney.value = journey
    }

    override suspend fun getSelectedJourney(): StateFlow<Journey?> = selectedJourney

    override suspend fun checkHasVehiclePosition(): Result<Boolean> {
        return selectedJourney.value?.detailsId?.let {detailsId ->
            journeysDatasource.getJourneyVehicles(
                detailsRef = detailsId,
                token = getToken(),
            ).mapCatching {
                it.isNotEmpty()
            }
        } ?: Result.failure(Throwable("No journey selected"))
    }

    override suspend fun startVehicleTracking() {
        while(true) {
            selectedJourney.value?.detailsId?.let {detailsId ->
                journeysDatasource.getJourneyVehicles(
                    detailsRef = detailsId,
                    token = getToken(),
                ).onSuccess {
                    trackedVehicle.emit(it)
                }
            }
            delay(3000)
        }
    }
    private fun updateSelectedJourney(legsDetails: List<LegDetails>) {
        val updatedLegs = selectedJourney.value?.legs?.map { leg ->
            try {
                legsDetails.first {
                    it.index == leg.index
                }.let {
                    leg.copy(details = it)
                }
            } catch (e: NoSuchElementException) {
                loge("JourneysRepositoryImpl: updateSelectedJourney: $e")
                leg
            }
        }
        updatedLegs?.let {
            selectedJourney.value = selectedJourney.value?.copy(legs = it)
        }
    }
}