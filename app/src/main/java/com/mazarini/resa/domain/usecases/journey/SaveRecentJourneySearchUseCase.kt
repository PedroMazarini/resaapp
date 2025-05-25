package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.JourneySearch
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface SaveRecentJourneySearchUseCase {
    suspend operator fun invoke(journeySearch: JourneySearch)

    companion object Factory
}

fun SaveRecentJourneySearchUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): SaveRecentJourneySearchUseCase = SaveRecentJourneySearchUseCaseImpl(journeysRepository)

private class SaveRecentJourneySearchUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : SaveRecentJourneySearchUseCase {
    override suspend fun invoke(journeySearch: JourneySearch) {
        journeysRepository.saveRecentJourneySearch(journeySearch)
    }
}
