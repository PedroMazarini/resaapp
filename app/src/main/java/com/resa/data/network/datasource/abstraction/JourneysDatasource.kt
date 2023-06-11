package com.resa.data.network.datasource.abstraction

import com.resa.data.network.model.journeys.QueryJourneysParams
import com.resa.data.network.requestHandlers.ApiResult
import com.resa.domain.model.JourneysCollection

interface JourneysDatasource {

    suspend fun queryJourneys(
        journeysParams: QueryJourneysParams,
        token: String,
    ): ApiResult<JourneysCollection?>
}