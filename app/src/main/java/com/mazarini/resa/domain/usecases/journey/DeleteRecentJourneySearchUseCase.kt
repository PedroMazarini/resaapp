package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface DeleteRecentJourneySearchUseCase {
    suspend operator fun invoke(
        id: String,
    )

    companion object Factory
}

fun DeleteRecentJourneySearchUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): DeleteRecentJourneySearchUseCase = DeleteRecentJourneySearchUseCaseImpl(journeysRepository)

private class DeleteRecentJourneySearchUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : DeleteRecentJourneySearchUseCase {
    override suspend fun invoke(
        id: String,
    ) {
        journeysRepository.deleteRecentJourneySearch(id)
    }
}
