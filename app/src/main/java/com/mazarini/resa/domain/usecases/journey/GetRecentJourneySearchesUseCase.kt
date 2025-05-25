package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.JourneySearch
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import kotlinx.coroutines.flow.Flow

interface GetRecentJourneySearchesUseCase {
    suspend operator fun invoke(): Flow<List<JourneySearch>>

    companion object Factory
}

fun GetRecentJourneySearchesUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): GetRecentJourneySearchesUseCase = GetRecentJourneySearchesUseCaseImpl(journeysRepository)

private class GetRecentJourneySearchesUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : GetRecentJourneySearchesUseCase {
    override suspend fun invoke() =
        journeysRepository.getAllRecentJourneySearch()
}
