package com.mazarini.resa.domain.usecases.stoparea

import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository
import com.mazarini.resa.global.fake.FakeFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GetStopsByCoordinateUseCaseTest {
    private lateinit var repository: StopsRepository
    private lateinit var useCase: GetStopsByCoordinateUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetStopsByCoordinateUseCase.Factory.build(repository)
    }

    @Test
    fun `invoke calls repository and returns result`() = runTest {
        // Given
        val coordinate = Coordinate(1.0, 2.0)
        val stops = listOf(FakeFactory.domainLocation())
        coEvery { repository.getStopsByCoordinate(coordinate) } returns stops
        // When
        val result = useCase(coordinate)
        // Then
        coVerify { repository.getStopsByCoordinate(coordinate) }
        assertEquals(stops, result)
    }
}

