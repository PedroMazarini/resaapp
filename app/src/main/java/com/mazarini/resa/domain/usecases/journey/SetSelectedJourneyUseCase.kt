package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface SetSelectedJourneyUseCase {
    operator fun invoke(journey: Journey)

    companion object Factory
}

fun SetSelectedJourneyUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): SetSelectedJourneyUseCase = SetSelectedJourneyUseCaseImpl(journeysRepository)

private class SetSelectedJourneyUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : SetSelectedJourneyUseCase {
    override fun invoke(journey: Journey) {
        journeysRepository.setSelectedJourney(journey)
    }
}
