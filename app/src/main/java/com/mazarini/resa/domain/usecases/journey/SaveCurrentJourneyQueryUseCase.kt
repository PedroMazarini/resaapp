package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface SaveCurrentJourneyQueryUseCase {
    suspend operator fun invoke(
        queryJourneysParams: QueryJourneysParams,
    )

    companion object Factory
}

fun SaveCurrentJourneyQueryUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): SaveCurrentJourneyQueryUseCase = SaveCurrentJourneyQueryUseCaseImpl(journeysRepository)

private class SaveCurrentJourneyQueryUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : SaveCurrentJourneyQueryUseCase {
    override suspend fun invoke(
        queryJourneysParams: QueryJourneysParams
    ) {
        journeysRepository.saveCurrentJourneyQuery(queryJourneysParams)
    }
}