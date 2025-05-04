package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface ClearOldJourneySearchesUseCase {
    suspend operator fun invoke()
    companion object Factory
}

fun ClearOldJourneySearchesUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): ClearOldJourneySearchesUseCase = ClearOldJourneySearchesUseCaseImpl(journeysRepository)

private class ClearOldJourneySearchesUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : ClearOldJourneySearchesUseCase {
    override suspend fun invoke() {
        journeysRepository.clearOldJourneySearches()
    }
}

