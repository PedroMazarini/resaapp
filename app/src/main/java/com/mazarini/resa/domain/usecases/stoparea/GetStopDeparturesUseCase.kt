package com.mazarini.resa.domain.usecases.stoparea

import com.mazarini.resa.domain.model.stoparea.StopJourney
import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository
import javax.inject.Inject

interface GetStopDeparturesUseCase {
    suspend operator fun invoke(stopAreaGid: String): List<StopJourney>
}

class GetStopDeparturesUseCaseImpl
@Inject
constructor(
    private val stopsRepository: StopsRepository,
) : GetStopDeparturesUseCase {
    override suspend fun invoke(stopAreaGid: String) =
        stopsRepository.getStopDepartures(stopAreaGid)
}
