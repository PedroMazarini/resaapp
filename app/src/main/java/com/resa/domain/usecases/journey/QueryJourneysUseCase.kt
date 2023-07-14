package com.resa.domain.usecases.journey

import androidx.paging.PagingData
import com.resa.domain.model.journey.Journey
import com.resa.domain.repositoryAbstraction.JourneysRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface QueryJourneysUseCase {
    suspend operator fun invoke(): Flow<PagingData<Journey>>
}

class QueryJourneysUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : QueryJourneysUseCase {
    override suspend fun invoke(): Flow<PagingData<Journey>> {
        return journeysRepository.queryJourneys()
    }
}