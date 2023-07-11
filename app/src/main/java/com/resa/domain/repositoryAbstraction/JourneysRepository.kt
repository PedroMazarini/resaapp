package com.resa.domain.repositoryAbstraction

import androidx.paging.PagingData
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import kotlinx.coroutines.flow.Flow

interface JourneysRepository {

    suspend fun queryJourneys(): Flow<PagingData<Journey>>

    suspend fun saveCurrentJourneyQuery(
        queryJourneysParams: QueryJourneysParams,
    )

    suspend fun getCurrentJourneyQuery(): Result<QueryJourneysParams>
}