package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface GetCurrentJourneyQueryUseCase {
    suspend operator fun invoke(): Result<QueryJourneysParams>

    companion object Factory
}

fun GetCurrentJourneyQueryUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): GetCurrentJourneyQueryUseCase = GetCurrentJourneyQueryUseCaseImpl(journeysRepository)

private class GetCurrentJourneyQueryUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : GetCurrentJourneyQueryUseCase {
    override suspend fun invoke() = journeysRepository.getCurrentJourneyQuery()
}