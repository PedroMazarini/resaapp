package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class StartVehicleTrackingUseCaseTest {
    private lateinit var repository: JourneysRepository
    private lateinit var useCase: StartVehicleTrackingUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = StartVehicleTrackingUseCase.Factory.build(repository)
    }

    @Test
    fun `invoke calls repository and returns result`() = runTest {
        // Given
        coEvery { repository.startVehicleTracking() } just Runs
        // When
        useCase()
        // Then
        coVerify { repository.startVehicleTracking() }
    }
}

