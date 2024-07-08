package com.mazarini.resa.data.network.datasource.abstraction

import androidx.paging.PagingData
import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.model.journey.LegDetails
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
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

    suspend fun getJourneyDetails(
        detailsRef: String,
        token: String,
    ): Result<List<LegDetails>>

    suspend fun getJourneyVehicles(
        detailsRef: String,
        token: String,
    ): Result<List<VehiclePosition>>
}