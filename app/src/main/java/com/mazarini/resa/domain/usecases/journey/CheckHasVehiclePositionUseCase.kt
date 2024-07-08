package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import javax.inject.Inject

interface CheckHasVehiclePositionUseCase {
    suspend operator fun invoke(): Result<Boolean>
}

class CheckHasVehiclePositionUseCaseImpl
@Inject
constructor(
    private val journeysRepository: JourneysRepository,
) : CheckHasVehiclePositionUseCase {
    override suspend fun invoke() = journeysRepository.checkHasVehiclePosition()
}