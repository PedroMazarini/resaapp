package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface DeleteSavedJourneySearchUseCase {
    suspend operator fun invoke(
        id: String,
    )
}

class DeleteSavedJourneySearchUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : DeleteSavedJourneySearchUseCase {
    override suspend fun invoke(
        id: String,
    ) {
        journeysRepository.deleteSavedJourneySearch(id)
    }
}
