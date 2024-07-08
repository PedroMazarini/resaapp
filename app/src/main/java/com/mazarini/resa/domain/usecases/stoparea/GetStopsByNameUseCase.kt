package com.mazarini.resa.domain.usecases.stoparea

import com.mazarini.resa.domain.repositoryAbstraction.DomainStopAreas
import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository
import javax.inject.Inject

interface GetStopsByNameUseCase {
    suspend operator fun invoke(query: String): DomainStopAreas
}

class GetStopsByNameUseCaseImpl
@Inject
constructor(
    private val stopsRepository: StopsRepository,
) : GetStopsByNameUseCase {
    override suspend fun invoke(query: String) =
        stopsRepository.getStopsByName(query)
}
