package com.mazarini.resa.domain.usecases.stoparea

import com.mazarini.resa.domain.repositoryAbstraction.DomainStopAreas
import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository

interface GetStopsByNameUseCase {
    suspend operator fun invoke(query: String): DomainStopAreas

    companion object Factory
}

fun GetStopsByNameUseCase.Factory.build(
    stopsRepository: StopsRepository,
): GetStopsByNameUseCase = GetStopsByNameUseCaseImpl(stopsRepository)

private class GetStopsByNameUseCaseImpl(
    private val stopsRepository: StopsRepository,
) : GetStopsByNameUseCase {
    override suspend fun invoke(query: String) =
        stopsRepository.getStopsByName(query)
}
