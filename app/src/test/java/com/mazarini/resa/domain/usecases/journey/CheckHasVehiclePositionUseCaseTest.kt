package com.mazarini.resa.domain.usecases.journey

import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class CheckHasVehiclePositionUseCaseTest {
    private lateinit var repository: JourneysRepository
    private lateinit var useCase: CheckHasVehiclePositionUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = CheckHasVehiclePositionUseCase.Factory.build(repository)
    }

    @Test
    fun `invoke returns repository result`() = runTest {
        // Given
        coEvery { repository.checkHasVehiclePosition() } returns Result.success(true)
        // When
        val result = useCase()
        // Then
        coVerify { repository.checkHasVehiclePosition() }
        assertEquals(Result.success(true), result)
    }
}

