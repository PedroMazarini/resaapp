package com.resa.domain.usecases.journey

import com.resa.domain.model.JourneySearch
import com.resa.domain.repositoryAbstraction.JourneysRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetRecentJourneySearchesUseCase {
    suspend operator fun invoke(): Flow<List<JourneySearch>>
}

class GetRecentJourneySearchesUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : GetRecentJourneySearchesUseCase {
    override suspend fun invoke() =
        journeysRepository.getAllRecentJourneySearch()
}
