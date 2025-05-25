package com.mazarini.resa.domain.usecases.location

import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.model.LocationType
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
import kotlinx.coroutines.flow.Flow

interface GetRecentLocationsUseCase {
    suspend operator fun invoke(
        types: List<LocationType> = LocationType.entries,
        limit: Int = 100,
    ): Flow<List<Location>>

    companion object Factory
}

fun GetRecentLocationsUseCase.Factory.build(
    locationsRepository: LocationsRepository,
): GetRecentLocationsUseCase = GetRecentLocationsUseCaseImpl(locationsRepository)

private class GetRecentLocationsUseCaseImpl(
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
