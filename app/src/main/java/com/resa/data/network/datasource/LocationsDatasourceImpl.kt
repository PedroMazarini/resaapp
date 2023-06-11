package com.resa.data.network.datasource

import com.resa.data.network.datasource.abstraction.LocationsDatasource
import com.resa.data.network.mappers.QueryJourneysParamsMapper
import com.resa.data.network.mappers.QueryLocationsParamsMapper
import com.resa.data.network.mappers.RemoteJourneysResponseToDomain
import com.resa.data.network.mappers.RemoteLocationToDomain
import com.resa.data.network.mappers.RemoteLocationsResponseToDomain
import com.resa.data.network.model.journeys.QueryJourneysParams
import com.resa.data.network.model.journeys.transportModesNames
import com.resa.data.network.model.journeys.transportSubModesNames
import com.resa.data.network.model.location.QueryLocationsParams
import com.resa.data.network.model.location.QueryLocationsParams.*
import com.resa.data.network.model.location.typesNames
import com.resa.data.network.requestHandlers.ApiResult
import com.resa.data.network.requestHandlers.safeApiCall
import com.resa.data.network.services.JourneysService
import com.resa.data.network.services.LocationsService
import com.resa.data.network.services.RetrofitService
import com.resa.domain.model.LocationCollection
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import com.resa.domain.model.JourneysCollection as DomainJourneysResponse

class LocationsDatasourceImpl
@Inject
constructor(
    private val retrofitService: RetrofitService,
    private val locationsParamsMapper: QueryLocationsParamsMapper,
    private val remoteLocationsResponseToDomain: RemoteLocationsResponseToDomain,
) : LocationsDatasource {

    override suspend fun queryLocations(
        queryLocationsParams: QueryLocationsParams,
        token: String
    ): ApiResult<LocationCollection?> {
        val queryMap = locationsParamsMapper.map(queryLocationsParams)

        return safeApiCall(Dispatchers.IO) {
            val response = retrofitService.getInstance(LocationsService::class.java)
                .queryLocation(
                    auth = "Bearer $token",
                    queryMode = queryLocationsParams.queryMode,
                    queryMap = queryMap,
                    types = queryLocationsParams.typesNames(),
                )
            remoteLocationsResponseToDomain.map(response)
        }
    }
}
