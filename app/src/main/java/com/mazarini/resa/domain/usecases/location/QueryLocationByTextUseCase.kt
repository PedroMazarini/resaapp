package com.mazarini.resa.domain.usecases.location

import androidx.paging.PagingData
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
import kotlinx.coroutines.flow.Flow
import com.mazarini.resa.domain.model.Location as DomainLocation

interface QueryLocationByTextUseCase {
    suspend operator fun invoke(
        query: String,
    ): Flow<PagingData<DomainLocation>>

    companion object Factory
}

fun QueryLocationByTextUseCase.Factory.build(
    locationsRepository: LocationsRepository,
): QueryLocationByTextUseCase = QueryLocationByTextUseCaseImpl(locationsRepository)

private class QueryLocationByTextUseCaseImpl(
    private val locationsRepository: LocationsRepository,
) : QueryLocationByTextUseCase {
    override suspend fun invoke(
        query: String,
    ): Flow<PagingData<DomainLocation>> {
        return locationsRepository.queryLocationByText(query)
    }
}