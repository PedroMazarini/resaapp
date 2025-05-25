package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository

interface StartVehicleTrackingUseCase {
    suspend operator fun invoke()

    companion object Factory
}

fun StartVehicleTrackingUseCase.Factory.build(
    journeysRepository: JourneysRepository,
): StartVehicleTrackingUseCase = StartVehicleTrackingUseCaseImpl(journeysRepository)

private class StartVehicleTrackingUseCaseImpl(
    private val journeysRepository: JourneysRepository,
) : StartVehicleTrackingUseCase {
    override suspend fun invoke() = journeysRepository.startVehicleTracking()
}