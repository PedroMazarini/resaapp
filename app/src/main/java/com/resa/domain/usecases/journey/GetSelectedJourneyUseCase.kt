package com.resa.domain.usecases.journey

import com.resa.domain.model.journey.Journey
import com.resa.domain.repositoryAbstraction.JourneysRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface GetSelectedJourneyUseCase {
    suspend operator fun invoke(): StateFlow<Journey?>
}

class GetSelectedJourneyUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : GetSelectedJourneyUseCase {
    override suspend fun invoke(): StateFlow<Journey?> = journeysRepository.getSelectedJourney()
}
