package com.mazarini.resa.domain.usecases.stoparea

import com.mazarini.resa.domain.model.stoparea.StopJourney
import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository

interface GetStopDeparturesUseCase {
    suspend operator fun invoke(stopAreaGid: String): List<StopJourney>

    companion object Factory
}

fun GetStopDeparturesUseCase.Factory.build(
    stopsRepository: StopsRepository,
): GetStopDeparturesUseCase = GetStopDeparturesUseCaseImpl(stopsRepository)

private class GetStopDeparturesUseCaseImpl(
    private val stopsRepository: StopsRepository,
) : GetStopDeparturesUseCase {
    override suspend fun invoke(stopAreaGid: String) =
        stopsRepository.getStopDepartures(stopAreaGid)
}
