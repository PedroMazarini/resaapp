package com.mazarini.resa.domain.usecases.location

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

class DeleteSavedLocationUseCaseTest {
    private lateinit var repository: LocationsRepository
    private lateinit var useCase: DeleteSavedLocationUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteSavedLocationUseCase.Factory.build(repository)
    }

    @Test
    fun `invoke calls repository and returns result`() = runTest {
        // Given
        val id = "locationId"
        coEvery { repository.deleteSavedLocation(id) } just Runs
        // When
        useCase(id)
        // Then
        coVerify { repository.deleteSavedLocation(id) }
    }
}

