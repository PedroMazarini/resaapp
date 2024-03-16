package com.resa.domain.usecases.journey

import com.resa.domain.repositoryAbstraction.JourneysRepository
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
