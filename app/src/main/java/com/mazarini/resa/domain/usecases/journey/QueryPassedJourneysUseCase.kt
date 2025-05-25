package com.mazarini.resa.domain.usecases.journey

import androidx.paging.PagingData
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import kotlinx.coroutines.flow.Flow

interface QueryPassedJourneysUseCase {
    suspend operator fun invoke(): Flow<PagingData<Journey>>

    companion object Factory
}

fun QueryPassedJourneysUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): QueryPassedJourneysUseCase = QueryPassedJourneysUseCaseImpl(journeysRepository)

private class QueryPassedJourneysUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : QueryPassedJourneysUseCase {
    override suspend fun invoke(): Flow<PagingData<Journey>> {
        return journeysRepository.queryPassedJourneys()
    }
}