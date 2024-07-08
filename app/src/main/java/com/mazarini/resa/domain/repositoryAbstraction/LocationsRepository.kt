package com.mazarini.resa.domain.repositoryAbstraction

import androidx.paging.PagingData
import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.model.LocationType
import com.mazarini.resa.domain.model.Location as DomainLocation
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    suspend fun queryLocationByText(
        query: String,
    ): Flow<PagingData<DomainLocation>>

    suspend fun saveLocation(location: Location)
    suspend fun deleteSavedLocation(id: String)
    suspend fun getAllSavedLocation(): Flow<List<Location>>

    suspend fun saveRecentLocation(location: Location)
    suspend fun getAllRecentLocation(
        limit: Int,
        types: List<LocationType>,
    ): Flow<List<Location>>
}