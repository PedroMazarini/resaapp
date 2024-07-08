package com.mazarini.resa.domain.usecases.location

import androidx.paging.PagingData
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.mazarini.resa.domain.model.Location as DomainLocation

interface QueryLocationByTextUseCase {
    suspend operator fun invoke(
        query: String,
    ): Flow<PagingData<DomainLocation>>
}

class QueryLocationByTextUseCaseImpl
@Inject
constructor(
    private val locationsRepository: LocationsRepository,
) : QueryLocationByTextUseCase {
    override suspend fun invoke(
        query: String,
    ): Flow<PagingData<DomainLocation>> {
        return locationsRepository.queryLocationByText(query)
    }
}