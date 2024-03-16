package com.resa.domain.usecases.journey

import com.resa.domain.model.journey.Journey
import com.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface SetSelectedJourneyUseCase {
    operator fun invoke(journey: Journey)
}

class SetSelectedJourneyUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : SetSelectedJourneyUseCase {
    override fun invoke(journey: Journey) {
        journeysRepository.setSelectedJourney(journey)
    }
}
