package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface SaveCurrentJourneyToHomeUseCase {
    suspend operator fun invoke()
}

class SaveCurrentJourneyToHomeUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : SaveCurrentJourneyToHomeUseCase {
    override suspend fun invoke() = journeysRepository.saveJourneyToHome()
}
