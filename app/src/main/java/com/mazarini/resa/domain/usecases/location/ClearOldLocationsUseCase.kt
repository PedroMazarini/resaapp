package com.mazarini.resa.domain.usecases.location

import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository

interface ClearOldLocationsUseCase {
    suspend operator fun invoke()

    companion object Factory
}

fun ClearOldLocationsUseCase.Factory.build(
    locationsRepository: LocationsRepository,
): ClearOldLocationsUseCase = ClearOldLocationsUseCaseImpl(locationsRepository)

private class ClearOldLocationsUseCaseImpl(
    private val locationsRepository: LocationsRepository,
) : ClearOldLocationsUseCase {
    override suspend fun invoke() {
        locationsRepository.clearOldLocations()
    }
}

