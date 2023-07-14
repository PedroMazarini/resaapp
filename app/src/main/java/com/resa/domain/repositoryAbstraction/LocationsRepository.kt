package com.resa.domain.repositoryAbstraction

import androidx.paging.PagingData
import com.resa.domain.model.Location
import com.resa.domain.model.Location as DomainLocation
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    suspend fun queryLocationByText(
        query: String,
    ): Flow<PagingData<DomainLocation>>

    suspend fun saveLocation(location: Location)
    suspend fun deleteSavedLocation(id: String)
    suspend fun getAllSavedLocation(): Flow<List<Location>>

    suspend fun saveRecentLocation(location: Location)
    suspend fun getAllRecentLocation(): Flow<List<Location>>
}