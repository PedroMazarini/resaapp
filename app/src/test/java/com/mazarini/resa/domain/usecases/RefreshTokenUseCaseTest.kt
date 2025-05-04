package com.mazarini.resa.domain.usecases

import com.mazarini.resa.data.repository.AuthenticationRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RefreshTokenUseCaseTest {
    private lateinit var repository: AuthenticationRepository
    private lateinit var useCase: RefreshTokenUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = RefreshTokenUseCase.Factory.build(repository)
    }

    @Test
    fun `invoke calls repository and returns result`() = runTest {
        // Given
        coEvery { repository.refreshToken() } just Runs
        // When
        useCase()
        // Then
        coVerify { repository.refreshToken() }
    }
}

