package com.mazarini.resa.domain.usecases.location

import androidx.paging.PagingData
import com.mazarini.resa.domain.model.Location
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

class QueryLocationByTextUseCaseTest {
    private lateinit var repository: LocationsRepository
    private lateinit var useCase: QueryLocationByTextUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = QueryLocationByTextUseCase.Factory.build(repository)
    }

    @Test
    fun `invoke calls repository and returns result`() = runTest {
        // Given
        val query = "Central"
        val locations = flowOf(PagingData.from(listOf(FakeFactory.domainLocation())))
        coEvery { repository.queryLocationByText(query) } returns locations
        // When
        val result = useCase(query)
        // Then
        coVerify { repository.queryLocationByText(query) }
        assertEquals(locations, result)
    }
}

