package com.resa.domain.usecases.location

import com.resa.domain.repositoryAbstraction.LocationsRepository
import javax.inject.Inject

interface DeleteSavedLocationUseCase {
    suspend operator fun invoke(
        id: String,
    )
}

class DeleteSavedLocationUseCaseImpl
@Inject
constructor(
    private val locationsRepository: LocationsRepository,
) : DeleteSavedLocationUseCase {
    override suspend fun invoke(
        id: String,
    ) {
        locationsRepository.deleteSavedLocation(id)
    }
}
