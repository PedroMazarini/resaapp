package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface LoadSavedJourneyToHomeUseCase {
    suspend operator fun invoke()
}

class LoadSavedJourneyToHomeUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : LoadSavedJourneyToHomeUseCase {
    override suspend fun invoke() = journeysRepository.loadSavedJourneyToHome()
}
