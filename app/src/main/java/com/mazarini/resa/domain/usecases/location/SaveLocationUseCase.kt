package com.mazarini.resa.domain.usecases.location

import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository

interface SaveLocationUseCase {
    suspend operator fun invoke(
        location: Location,
    )

    companion object Factory
}

fun SaveLocationUseCase.Factory.build(
    locationsRepository: LocationsRepository,
): SaveLocationUseCase = SaveLocationUseCaseImpl(locationsRepository)

private class SaveLocationUseCaseImpl(
    private val locationsRepository: LocationsRepository,
) : SaveLocationUseCase {
    override suspend fun invoke(
        location: Location,
    ) {
        locationsRepository.saveLocation(location)
    }
}
