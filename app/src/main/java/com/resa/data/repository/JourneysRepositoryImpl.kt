package com.resa.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.resa.data.cache.service.RecentJourneySearchCacheService
import com.resa.data.cache.service.SavedJourneySearchCacheService
import com.resa.data.network.datasource.abstraction.JourneysDatasource
import com.resa.domain.model.JourneySearch
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.journey.Leg
import com.resa.domain.model.journey.LegDetails
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import com.resa.domain.repositoryAbstraction.JourneysRepository
import com.resa.global.JsonEncoder
import com.resa.global.preferences.PrefsProvider
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
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

    init {
        clearOldJourneySearches()
    }

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

    private suspend fun getToken() = prefsProvider.getToken()

    private suspend fun getCurrentJourneysQuery() = JsonEncoder.decode<QueryJourneysParams>(
        prefsProvider.getCurrentJourneysQuery().orEmpty()
    )

    override suspend fun saveJourneySearch(journeySearch: JourneySearch) {
        savedJourneySearchService.saveJourneySearch(journeySearch)
    }

    override suspend fun deleteSavedJourneySearch(id: Int) {
        savedJourneySearchService.deleteJourneySearch(id)
    }

    override suspend fun getAllSavedJourneySearch(): Flow<List<JourneySearch>> =
        savedJourneySearchService.getAllJourneySearches()

    override suspend fun saveRecentJourneySearch(journeySearch: JourneySearch) {
        recentJourneySearchService.saveJourneySearch(journeySearch)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun clearOldJourneySearches() {
        GlobalScope.launch(Dispatchers.IO) {
            recentJourneySearchService.clearOldJourneySearch()
        }
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

    private fun updateSelectedJourney(legsDetails: List<LegDetails>) {
        val updatedLegs = selectedJourney.value?.legs?.map { leg ->
            when (leg) {
                is Leg.Transport -> {
                    val details = legsDetails.firstOrNull {
                        it is LegDetails.Details && it.index == leg.index
                    } ?: leg.details
                    leg.copy(details = details)
                }
                else -> leg
            }
        }
        updatedLegs?.let {
            selectedJourney.value = selectedJourney.value?.copy(legs = it)
        }
    }
}