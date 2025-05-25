package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface FetchSelectedJourneyDetailsUseCase {
    suspend operator fun invoke()

    companion object Factory
}

fun FetchSelectedJourneyDetailsUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): FetchSelectedJourneyDetailsUseCase = FetchSelectedJourneyDetailsUseCaseImpl(journeysRepository)

private class FetchSelectedJourneyDetailsUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : FetchSelectedJourneyDetailsUseCase {
    override suspend fun invoke() { journeysRepository.fetchSelectedJourneyDetails() }
}
