package com.resa.data.network.datasource

import com.resa.data.network.datasource.abstraction.JourneysDatasource
import com.resa.data.network.mappers.QueryJourneysParamsMapper
import com.resa.data.network.mappers.RemoteJourneysResponseToDomain
import com.resa.data.network.model.journeys.QueryJourneysParams
import com.resa.data.network.model.journeys.transportModesNames
import com.resa.data.network.model.journeys.transportSubModesNames
import com.resa.data.network.requestHandlers.ApiResult
import com.resa.data.network.requestHandlers.safeApiCall
import com.resa.data.network.services.JourneysService
import com.resa.data.network.services.RetrofitService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import com.resa.domain.model.JourneysCollection as DomainJourneysResponse

class JourneysDatasourceImpl
@Inject
constructor(
    private val retrofitService: RetrofitService,
    private val journeysParamsMapper: QueryJourneysParamsMapper,
    private val remoteJourneysResponseToDomain: RemoteJourneysResponseToDomain,
) : JourneysDatasource {

    override suspend fun queryJourneys(
        journeysParams: QueryJourneysParams,
        token: String,
    ): ApiResult<DomainJourneysResponse?> {
        val queryMap = journeysParamsMapper.map(journeysParams)

        return safeApiCall(Dispatchers.IO) {
            val response = retrofitService.getInstance(JourneysService::class.java)
                .queryJourneys(
                    auth = "Bearer $token",
                    queryMap = queryMap,
                    transportModes = journeysParams.transportModesNames(),
                    transportSubModes = journeysParams.transportSubModesNames(),
                )
            remoteJourneysResponseToDomain.map(response)
        }
    }
}
