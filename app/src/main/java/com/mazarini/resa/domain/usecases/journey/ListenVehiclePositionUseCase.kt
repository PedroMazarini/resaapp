package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import kotlinx.coroutines.flow.Flow

interface ListenVehiclePositionUseCase {
    suspend operator fun invoke(): Flow<List<VehiclePosition>>

    companion object Factory
}

fun ListenVehiclePositionUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): ListenVehiclePositionUseCase = ListenVehiclePositionUseCaseImpl(journeysRepository)

private class ListenVehiclePositionUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : ListenVehiclePositionUseCase {
    override suspend fun invoke() = journeysRepository.listenVehiclePosition()
}