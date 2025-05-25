package com.mazarini.resa.domain.usecases.stoparea

import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.repositoryAbstraction.DomainStopAreas
import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository

interface GetStopsByCoordinateUseCase {
    suspend operator fun invoke(coordinate: Coordinate): DomainStopAreas

    companion object Factory
}

fun GetStopsByCoordinateUseCase.Factory.build(
    stopsRepository: StopsRepository,
): GetStopsByCoordinateUseCase = GetStopsByCoordinateUseCaseImpl(stopsRepository)

private class GetStopsByCoordinateUseCaseImpl(
    private val stopsRepository: StopsRepository,
) : GetStopsByCoordinateUseCase {
    override suspend fun invoke(coordinate: Coordinate) =
        stopsRepository.getStopsByCoordinate(coordinate)
}
