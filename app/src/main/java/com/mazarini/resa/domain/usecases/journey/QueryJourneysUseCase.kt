package com.mazarini.resa.domain.usecases.journey

import androidx.paging.PagingData
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import kotlinx.coroutines.flow.Flow

interface QueryJourneysUseCase {
    suspend operator fun invoke(): Flow<PagingData<Journey>>

    companion object Factory
}

fun QueryJourneysUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): QueryJourneysUseCase = QueryJourneysUseCaseImpl(journeysRepository)

private class QueryJourneysUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : QueryJourneysUseCase {
    override suspend fun invoke(): Flow<PagingData<Journey>> {
        return journeysRepository.queryJourneys()
    }
}