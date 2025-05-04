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

class SaveRecentLocationUseCaseTest {
    private lateinit var repository: LocationsRepository
    private lateinit var useCase: SaveRecentLocationUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = SaveRecentLocationUseCase.Factory.build(repository)
    }

    @Test
    fun `invoke calls repository and returns result`() = runTest {
        // Given
        val location = mockk<Location>()
        coEvery { repository.saveRecentLocation(location) } just Runs
        // When
        useCase(location)
        // Then
        coVerify { repository.saveRecentLocation(location) }
    }
}

