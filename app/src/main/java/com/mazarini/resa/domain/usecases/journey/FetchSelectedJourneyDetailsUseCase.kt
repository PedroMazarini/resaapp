package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface FetchSelectedJourneyDetailsUseCase {
    suspend operator fun invoke()
}

class FetchSelectedJourneyDetailsUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : FetchSelectedJourneyDetailsUseCase {
    override suspend fun invoke() { journeysRepository.fetchSelectedJourneyDetails() }
}
