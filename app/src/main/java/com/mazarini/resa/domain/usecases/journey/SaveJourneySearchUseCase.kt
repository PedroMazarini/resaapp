package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.JourneySearch
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface SaveJourneySearchUseCase {
    suspend operator fun invoke(journeySearch: JourneySearch)

    companion object Factory
}

fun SaveJourneySearchUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): SaveJourneySearchUseCase = SaveJourneySearchUseCaseImpl(journeysRepository)

private class SaveJourneySearchUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : SaveJourneySearchUseCase {
    override suspend fun invoke(journeySearch: JourneySearch) {
        journeysRepository.saveJourneySearch(journeySearch)
    }
}
