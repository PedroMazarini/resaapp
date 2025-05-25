package com.mazarini.resa.domain.usecases.location

import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository

interface SaveRecentLocationUseCase {
    suspend operator fun invoke(
        location: Location,
    )

    companion object Factory
}

fun SaveRecentLocationUseCase.Factory.build(
    locationsRepository: LocationsRepository,
): SaveRecentLocationUseCase = SaveRecentLocationUseCaseImpl(locationsRepository)

private class SaveRecentLocationUseCaseImpl(
    private val locationsRepository: LocationsRepository,
) : SaveRecentLocationUseCase {
    override suspend fun invoke(
        location: Location,
    ) {
        locationsRepository.saveRecentLocation(location)
    }
}
