package com.resa.domain.usecases.location

import com.resa.domain.model.Location
import com.resa.domain.repositoryAbstraction.LocationsRepository
import javax.inject.Inject

interface SaveLocationUseCase {
    suspend operator fun invoke(
        location: Location,
    )
}

class SaveLocationUseCaseImpl
@Inject
constructor(
    private val locationsRepository: LocationsRepository,
) : SaveLocationUseCase {
    override suspend fun invoke(
        location: Location,
    ) {
        locationsRepository.saveLocation(location)
    }
}
