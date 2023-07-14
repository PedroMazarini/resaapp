package com.resa.domain.usecases.location

import com.resa.domain.model.Location
import com.resa.domain.repositoryAbstraction.LocationsRepository
import javax.inject.Inject

interface SaveRecentLocationUseCase {
    suspend operator fun invoke(
        location: Location,
    )
}

class SaveRecentLocationUseCaseImpl
@Inject
constructor(
    private val locationsRepository: LocationsRepository,
) : SaveRecentLocationUseCase {
    override suspend fun invoke(
        location: Location,
    ) {
        locationsRepository.saveRecentLocation(location)
    }
}
