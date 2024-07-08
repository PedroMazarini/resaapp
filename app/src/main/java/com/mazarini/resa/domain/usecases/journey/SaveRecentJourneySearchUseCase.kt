package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.JourneySearch
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface SaveRecentJourneySearchUseCase {
    suspend operator fun invoke(journeySearch: JourneySearch)
}

class SaveRecentJourneySearchUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : SaveRecentJourneySearchUseCase {
    override suspend fun invoke(journeySearch: JourneySearch) {
        journeysRepository.saveRecentJourneySearch(journeySearch)
    }
}
