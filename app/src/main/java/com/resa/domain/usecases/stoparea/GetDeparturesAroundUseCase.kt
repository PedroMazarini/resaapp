package com.resa.domain.usecases.stoparea

import com.resa.data.network.mappers.DomainStopPoints
import com.resa.domain.model.Coordinate
import com.resa.domain.repositoryAbstraction.StopPointsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetDeparturesAroundUseCase {
    suspend operator fun invoke(coordinate: Coordinate): Result<DomainStopPoints>
}

class GetDeparturesAroundUseCaseImpl
@Inject
constructor(
    private val stopPointsRepository: StopPointsRepository,
) : GetDeparturesAroundUseCase {
    override suspend fun invoke(coordinate: Coordinate) =
        stopPointsRepository.getDeparturesAround(coordinate)
}
