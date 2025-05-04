package com.mazarini.resa.domain.usecases.location

import com.mazarini.resa.domain.model.Location
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class SaveLocationUseCaseTest {
    private lateinit var repository: LocationsRepository
    private lateinit var useCase: SaveLocationUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = SaveLocationUseCase.Factory.build(repository)
    }

    @Test
    fun `invoke calls repository and returns result`() = runTest {
        // Given
        val location = mockk<Location>()
        coEvery { repository.saveLocation(location) } just Runs
        // When
        useCase(location)
        // Then
        coVerify { repository.saveLocation(location) }
    }
}

