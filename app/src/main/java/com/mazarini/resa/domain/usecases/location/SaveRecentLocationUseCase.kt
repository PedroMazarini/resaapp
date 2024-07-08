package com.mazarini.resa.domain.usecases.location

import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
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
