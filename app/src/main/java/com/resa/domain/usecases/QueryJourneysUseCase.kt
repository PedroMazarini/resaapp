package com.resa.domain.usecases

import com.resa.data.network.model.journeys.QueryJourneysParams
import com.resa.data.network.requestHandlers.ApiResponseHandler
import com.resa.data.network.requestHandlers.DataResult
import com.resa.data.repository.JourneysRepository
import com.resa.domain.model.JourneysCollection
import javax.inject.Inject

interface QueryJourneysUseCase {
    suspend operator fun invoke(
        queryJourneysParams: QueryJourneysParams,
    ): DataResult<JourneysCollection>
}

class QueryJourneysUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : QueryJourneysUseCase {
    override suspend fun invoke(
        queryJourneysParams: QueryJourneysParams
    ): DataResult<JourneysCollection> {
        val response = journeysRepository.queryJourneys(queryJourneysParams)

        return object : ApiResponseHandler<JourneysCollection>(response) {
            override suspend fun handleSuccess(
                resultData: JourneysCollection
            ): DataResult<JourneysCollection> {
                return DataResult.Success(resultData)
            }

        }.getResult()
    }
}