package com.mazarini.resa.domain.usecases.location

import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
import com.mazarini.resa.global.fake.FakeFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GetSavedLocationsUseCaseTest {
    private lateinit var repository: LocationsRepository
    private lateinit var useCase: GetSavedLocationsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetSavedLocationsUseCase.Factory.build(repository)
    }

    @Test
    fun `invoke calls repository and returns result`() = runTest {
        // Given
        val locations = flowOf(listOf(FakeFactory.domainLocation()))
        coEvery { repository.getAllSavedLocation() } returns locations
        // When
        val result = useCase()
        // Then
        coVerify { repository.getAllSavedLocation() }
        assertEquals(locations, result)
    }
}

