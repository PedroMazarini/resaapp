package com.resa.domain.usecases.journey

import com.resa.domain.model.JourneySearch
import com.resa.domain.repositoryAbstraction.JourneysRepository
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
