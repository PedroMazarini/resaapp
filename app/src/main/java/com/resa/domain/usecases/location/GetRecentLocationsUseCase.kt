package com.resa.domain.usecases.location

import com.resa.domain.model.Location
import com.resa.domain.repositoryAbstraction.LocationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetRecentLocationsUseCase {
    suspend operator fun invoke(): Flow<List<Location>>
}

class GetRecentLocationsUseCaseImpl
@Inject
constructor(
    private val locationsRepository: LocationsRepository,
) : GetRecentLocationsUseCase {
    override suspend fun invoke() =
        locationsRepository.getAllRecentLocation()
}
