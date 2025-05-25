package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface CheckHasVehiclePositionUseCase {
    suspend operator fun invoke(): Result<Boolean>

    companion object Factory
}

fun CheckHasVehiclePositionUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): CheckHasVehiclePositionUseCase = CheckHasVehiclePositionUseCaseImpl(journeysRepository)

private class CheckHasVehiclePositionUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : CheckHasVehiclePositionUseCase {
    override suspend fun invoke() = journeysRepository.checkHasVehiclePosition()
}