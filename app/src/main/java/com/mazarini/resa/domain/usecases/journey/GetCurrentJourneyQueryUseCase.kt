package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface GetCurrentJourneyQueryUseCase {
    suspend operator fun invoke(): Result<QueryJourneysParams>
}

class GetCurrentJourneyQueryUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : GetCurrentJourneyQueryUseCase {
    override suspend fun invoke() = journeysRepository.getCurrentJourneyQuery()
}