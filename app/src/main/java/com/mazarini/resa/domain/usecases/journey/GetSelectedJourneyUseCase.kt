package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import kotlinx.coroutines.flow.StateFlow

interface GetSelectedJourneyUseCase {
    suspend operator fun invoke(): StateFlow<Journey?>

    companion object Factory
}

fun GetSelectedJourneyUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): GetSelectedJourneyUseCase = GetSelectedJourneyUseCaseImpl(journeysRepository)

private class GetSelectedJourneyUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : GetSelectedJourneyUseCase {
    override suspend fun invoke(): StateFlow<Journey?> = journeysRepository.getSelectedJourney()
}
