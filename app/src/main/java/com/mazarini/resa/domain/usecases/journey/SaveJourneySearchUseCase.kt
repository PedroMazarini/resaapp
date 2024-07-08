package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.JourneySearch
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
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
