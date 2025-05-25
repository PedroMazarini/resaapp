package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.JourneySearch
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import kotlinx.coroutines.flow.Flow

interface GetSavedJourneySearchesUseCase {
    suspend operator fun invoke(): Flow<List<JourneySearch>>

    companion object Factory
}

fun GetSavedJourneySearchesUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): GetSavedJourneySearchesUseCase = GetSavedJourneySearchesUseCaseImpl(journeysRepository)

private class GetSavedJourneySearchesUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : GetSavedJourneySearchesUseCase {
    override suspend fun invoke() =
        journeysRepository.getAllSavedJourneySearch()
}
