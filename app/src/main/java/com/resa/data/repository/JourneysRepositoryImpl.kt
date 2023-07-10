package com.resa.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingData
import com.resa.data.network.datasource.abstraction.JourneysDatasource
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import com.resa.domain.repositoryAbstraction.JourneysRepository
import com.resa.global.JsonEncoder
import com.resa.global.preferences.PrefsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JourneysRepositoryImpl
@Inject
constructor(
    private val journeysDatasource: JourneysDatasource,
    private val prefsProvider: PrefsProvider,
) : JourneysRepository {

    @WorkerThread
    override suspend fun queryJourneys(): Flow<PagingData<Journey>> =
        journeysDatasource.queryJourneys(
            journeysParams = getCurrentJourneysQuery(),
            token = getToken(),
        ).flowOn(Dispatchers.IO)

    override suspend fun saveCurrentJourneyQuery(queryJourneysParams: QueryJourneysParams) {
        prefsProvider.setCurrentJourneysQuery(JsonEncoder.encode(queryJourneysParams).orEmpty())
    }

    private suspend fun getToken() = prefsProvider.getToken()

    private suspend fun getCurrentJourneysQuery() = JsonEncoder.decode<QueryJourneysParams>(
        prefsProvider.getCurrentJourneysQuery().orEmpty()
    )
}