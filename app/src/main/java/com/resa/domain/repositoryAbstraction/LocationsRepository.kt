package com.resa.domain.repositoryAbstraction

import androidx.paging.PagingData
import com.resa.domain.model.Location as DomainLocation
import kotlinx.coroutines.flow.Flow

interface LocationsRepository {

    suspend fun queryLocationByText(
        query: String,
    ): Flow<PagingData<DomainLocation>>
}