package com.resa.domain.usecases

import com.resa.data.network.model.location.QueryLocationsParams
import com.resa.data.network.requestHandlers.ApiResponseHandler
import com.resa.data.network.requestHandlers.DataResult
import com.resa.data.repository.LocationsRepository
import com.resa.domain.model.LocationCollection
import javax.inject.Inject

interface QueryLocationsUseCase {
    suspend operator fun invoke(
        queryLocationsParams: QueryLocationsParams,
    ): DataResult<LocationCollection>
}

class QueryLocationsUseCaseImpl
@Inject
constructor(
    private val locationsRepository: LocationsRepository,
) : QueryLocationsUseCase {
    override suspend fun invoke(
        queryLocationsParams: QueryLocationsParams
    ): DataResult<LocationCollection> {
        val response = locationsRepository.queryLocations(queryLocationsParams)

        return object : ApiResponseHandler<LocationCollection>(response) {
            override suspend fun handleSuccess(
                resultData: LocationCollection
            ): DataResult<LocationCollection> {
                return DataResult.Success(resultData)
            }
        }.getResult()
    }
}