package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface LoadSavedJourneyToHomeUseCase {
    suspend operator fun invoke()

    companion object Factory
}

fun LoadSavedJourneyToHomeUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): LoadSavedJourneyToHomeUseCase = LoadSavedJourneyToHomeUseCaseImpl(journeysRepository)

private class LoadSavedJourneyToHomeUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : LoadSavedJourneyToHomeUseCase {
    override suspend fun invoke() = journeysRepository.loadSavedJourneyToHome()
}
