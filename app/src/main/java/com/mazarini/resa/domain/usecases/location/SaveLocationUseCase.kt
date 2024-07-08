package com.mazarini.resa.domain.usecases.location

import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
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
