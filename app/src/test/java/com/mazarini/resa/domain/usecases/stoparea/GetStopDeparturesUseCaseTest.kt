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

class GetStopDeparturesUseCaseTest {
    private lateinit var repository: StopsRepository
    private lateinit var useCase: GetStopDeparturesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetStopDeparturesUseCase.Factory.build(repository)
    }

    @Test
    fun `invoke calls repository and returns result`() = runTest {
        // Given
        val stopId = "stop123"
        val departures = FakeFactory.stopJourneyList()
        coEvery { repository.getStopDepartures(stopId) } returns departures
        // When
        val result = useCase(stopId)
        // Then
        coVerify { repository.getStopDepartures(stopId) }
        assertEquals(departures, result)
    }
}

