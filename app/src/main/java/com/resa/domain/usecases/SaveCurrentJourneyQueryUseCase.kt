package com.resa.domain.usecases

import com.resa.domain.model.queryjourneys.QueryJourneysParams
import com.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface SaveCurrentJourneyQueryUseCase {
    suspend operator fun invoke(
        queryJourneysParams: QueryJourneysParams,
    )
}

class SaveCurrentJourneyQueryUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : SaveCurrentJourneyQueryUseCase {
    override suspend fun invoke(
        queryJourneysParams: QueryJourneysParams
    ) {
        journeysRepository.saveCurrentJourneyQuery(queryJourneysParams)
    }
}