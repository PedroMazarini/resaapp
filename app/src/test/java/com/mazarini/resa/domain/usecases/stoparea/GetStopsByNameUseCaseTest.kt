package com.mazarini.resa.domain.usecases.stoparea

import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository
import com.mazarini.resa.global.fake.FakeFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GetStopsByNameUseCaseTest {
    private lateinit var repository: StopsRepository
    private lateinit var useCase: GetStopsByNameUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetStopsByNameUseCase.Factory.build(repository)
    }

    @Test
    fun `invoke calls repository and returns result`() = runTest {
        // Given
        val stops = listOf(FakeFactory.domainLocation())
        coEvery { repository.getStopsByName("query") } returns stops
        // When
        val result = useCase.invoke("query")
        // Then
        coVerify { repository.getStopsByName("query") }
        assertEquals(stops, result)
    }
}

