package com.resa.data.network.datasource.abstraction

import androidx.paging.PagingData
import com.resa.data.network.model.travelplanner.location.QueryLocationsParams
import com.resa.domain.model.Location as DomainLocation
import kotlinx.coroutines.flow.Flow

interface LocationsDatasource {

    suspend fun queryLocationsByText(
        queryLocationsParams: QueryLocationsParams.ByText,
        token: String,
    ): Flow<PagingData<DomainLocation>>
}