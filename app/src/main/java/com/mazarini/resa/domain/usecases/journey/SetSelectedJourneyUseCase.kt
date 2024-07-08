package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
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
