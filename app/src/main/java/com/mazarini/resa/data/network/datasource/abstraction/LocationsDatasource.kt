package com.mazarini.resa.data.network.datasource.abstraction

import androidx.paging.PagingData
import com.mazarini.resa.data.network.model.travelplanner.location.QueryLocationsParams
import com.mazarini.resa.domain.model.Location as DomainLocation
import kotlinx.coroutines.flow.Flow

interface LocationsDatasource {

    suspend fun queryLocationsByText(
        queryLocationsParams: QueryLocationsParams.ByText,
        token: String,
    ): Flow<PagingData<DomainLocation>>
}