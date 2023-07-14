package com.resa.domain.usecases.location

import com.resa.domain.model.Location
import com.resa.domain.repositoryAbstraction.LocationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetSavedLocationsUseCase {
    suspend operator fun invoke(): Flow<List<Location>>
}

class GetSavedLocationsUseCaseImpl
@Inject
constructor(
    private val locationsRepository: LocationsRepository,
) : GetSavedLocationsUseCase {
    override suspend fun invoke() =
        locationsRepository.getAllSavedLocation()
}
