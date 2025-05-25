package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface SaveCurrentJourneyToHomeUseCase {
    suspend operator fun invoke()

    companion object Factory
}

fun SaveCurrentJourneyToHomeUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): SaveCurrentJourneyToHomeUseCase = SaveCurrentJourneyToHomeUseCaseImpl(journeysRepository)

private class SaveCurrentJourneyToHomeUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : SaveCurrentJourneyToHomeUseCase {
    override suspend fun invoke() = journeysRepository.saveJourneyToHome()
}
