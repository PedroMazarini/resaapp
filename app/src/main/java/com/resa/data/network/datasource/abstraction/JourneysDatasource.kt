package com.resa.data.network.datasource.abstraction

import androidx.paging.PagingData
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import kotlinx.coroutines.flow.Flow

interface JourneysDatasource {

    suspend fun queryJourneys(
        journeysParams: QueryJourneysParams,
        token: String,
    ): Flow<PagingData<Journey>>

    suspend fun queryPassedJourneys(
        journeysParams: QueryJourneysParams,
        token: String,
    ): Flow<PagingData<Journey>>
}