package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.JourneySearch
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetSavedJourneySearchesUseCase {
    suspend operator fun invoke(): Flow<List<JourneySearch>>
}

class GetSavedJourneySearchesUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : GetSavedJourneySearchesUseCase {
    override suspend fun invoke() =
        journeysRepository.getAllSavedJourneySearch()
}
