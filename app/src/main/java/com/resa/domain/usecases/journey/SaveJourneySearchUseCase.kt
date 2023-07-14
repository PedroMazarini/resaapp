package com.resa.domain.usecases.journey

import com.resa.domain.model.JourneySearch
import com.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface SaveJourneySearchUseCase {
    suspend operator fun invoke(journeySearch: JourneySearch)
}

class SaveJourneySearchUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : SaveJourneySearchUseCase {
    override suspend fun invoke(journeySearch: JourneySearch) {
        journeysRepository.saveJourneySearch(journeySearch)
    }
}
