package com.mazarini.resa.domain.usecases.location

import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository

interface DeleteSavedLocationUseCase {
    suspend operator fun invoke(
        id: String,
    )

    companion object Factory
}

fun DeleteSavedLocationUseCase.Factory.build(
    locationsRepository: LocationsRepository,
): DeleteSavedLocationUseCase = DeleteSavedLocationUseCaseImpl(locationsRepository)

private class DeleteSavedLocationUseCaseImpl(
    private val locationsRepository: LocationsRepository,
) : DeleteSavedLocationUseCase {
    override suspend fun invoke(
        id: String,
    ) {
        locationsRepository.deleteSavedLocation(id)
    }
}
