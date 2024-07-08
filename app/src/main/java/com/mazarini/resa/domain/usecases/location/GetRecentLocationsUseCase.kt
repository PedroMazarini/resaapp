package com.mazarini.resa.domain.usecases.location

import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.model.LocationType
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetRecentLocationsUseCase {
    suspend operator fun invoke(
        types: List<LocationType> = LocationType.values().toList(),
        limit: Int = 100,
    ): Flow<List<Location>>
}

class GetRecentLocationsUseCaseImpl
@Inject
constructor(
    private val locationsRepository: LocationsRepository,
) : GetRecentLocationsUseCase {
    override suspend fun invoke(
        types: List<LocationType>,
        limit: Int,
    ) = locationsRepository.getAllRecentLocation(
        limit = limit,
        types = types,
    )
}
