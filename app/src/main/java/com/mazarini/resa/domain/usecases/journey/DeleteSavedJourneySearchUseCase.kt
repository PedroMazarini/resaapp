package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface DeleteSavedJourneySearchUseCase {
    suspend operator fun invoke(
        id: String,
    )

    companion object Factory
}

fun DeleteSavedJourneySearchUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): DeleteSavedJourneySearchUseCase = DeleteSavedJourneySearchUseCaseImpl(journeysRepository)

private class DeleteSavedJourneySearchUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : DeleteSavedJourneySearchUseCase {
    override suspend fun invoke(
        id: String,
    ) {
        journeysRepository.deleteSavedJourneySearch(id)
    }
}
