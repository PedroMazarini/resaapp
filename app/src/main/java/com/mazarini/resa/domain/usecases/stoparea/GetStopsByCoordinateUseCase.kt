package com.mazarini.resa.domain.usecases.stoparea

import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.repositoryAbstraction.DomainStopAreas
import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository
import javax.inject.Inject

interface GetStopsByCoordinateUseCase {
    suspend operator fun invoke(coordinate: Coordinate): DomainStopAreas
}

class GetStopsByCoordinateUseCaseImpl
@Inject
constructor(
    private val stopsRepository: StopsRepository,
) : GetStopsByCoordinateUseCase {
    override suspend fun invoke(coordinate: Coordinate) =
        stopsRepository.getStopsByCoordinate(coordinate)
}
