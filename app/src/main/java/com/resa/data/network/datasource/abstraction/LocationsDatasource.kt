package com.resa.data.network.datasource.abstraction

import com.resa.data.network.model.location.QueryLocationsParams
import com.resa.data.network.requestHandlers.ApiResult
import com.resa.domain.model.LocationCollection

interface LocationsDatasource {

    suspend fun queryLocations(
        queryLocationsParams: QueryLocationsParams,
        token: String,
    ): ApiResult<LocationCollection?>
}