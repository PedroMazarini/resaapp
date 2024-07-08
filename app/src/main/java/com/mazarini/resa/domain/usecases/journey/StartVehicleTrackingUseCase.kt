package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface StartVehicleTrackingUseCase {
    suspend operator fun invoke()
}

class StartVehicleTrackingUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : StartVehicleTrackingUseCase {
    override suspend fun invoke() = journeysRepository.startVehicleTracking()
}