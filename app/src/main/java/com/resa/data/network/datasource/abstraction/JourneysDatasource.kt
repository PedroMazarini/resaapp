package com.resa.data.network.datasource.abstraction

import androidx.paging.PagingData
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.journey.LegDetails
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface JourneysDatasource {

    suspend fun queryJourneys(
        journeysParams: QueryJourneysParams,
        token: String,
    ): Flow<PagingData<Journey>>

    suspend fun queryPassedJourneys(
        journeysParams: QueryJourneysParams,
        token: String,
    ): Flow<PagingData<Journey>>

    suspend fun getJourneyDetails(
        detailsRef: String,
        token: String,
    ): Result<List<LegDetails>>
}