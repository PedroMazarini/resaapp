package com.mazarini.resa.domain.usecases.location

import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
import kotlinx.coroutines.flow.Flow

interface GetSavedLocationsUseCase {
    suspend operator fun invoke(): Flow<List<Location>>

    companion object Factory
}

fun GetSavedLocationsUseCase.Factory.build(
    locationsRepository: LocationsRepository,
): GetSavedLocationsUseCase = GetSavedLocationsUseCaseImpl(locationsRepository)

private class GetSavedLocationsUseCaseImpl(
    private val locationsRepository: LocationsRepository,
) : GetSavedLocationsUseCase {
    override suspend fun invoke() =
        locationsRepository.getAllSavedLocation()
}
