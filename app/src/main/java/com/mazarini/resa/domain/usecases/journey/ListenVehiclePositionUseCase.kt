package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ListenVehiclePositionUseCase {
    suspend operator fun invoke(): Flow<List<VehiclePosition>>
}

class ListenVehiclePositionUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : ListenVehiclePositionUseCase {
    override suspend fun invoke() = journeysRepository.listenVehiclePosition()
}