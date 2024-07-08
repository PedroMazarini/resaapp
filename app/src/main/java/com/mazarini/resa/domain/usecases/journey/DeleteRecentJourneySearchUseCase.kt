package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface DeleteRecentJourneySearchUseCase {
    suspend operator fun invoke(
        id: String,
    )
}

class DeleteRecentJourneySearchUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : DeleteRecentJourneySearchUseCase {
    override suspend fun invoke(
        id: String,
    ) {
        journeysRepository.deleteRecentJourneySearch(id)
    }
}
