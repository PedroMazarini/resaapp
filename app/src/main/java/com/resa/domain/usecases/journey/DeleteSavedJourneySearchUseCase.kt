package com.resa.domain.usecases.journey

import com.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface DeleteSavedJourneySearchUseCase {
    suspend operator fun invoke(
        id: Int,
    )
}

class DeleteSavedJourneySearchUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : DeleteSavedJourneySearchUseCase {
    override suspend fun invoke(
        id: Int,
    ) {
        journeysRepository.deleteSavedJourneySearch(id)
    }
}
