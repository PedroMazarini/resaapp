package com.mazarini.resa.domain.usecases

import com.mazarini.resa.data.repository.AuthenticationRepository

interface RefreshTokenUseCase {
    suspend operator fun invoke()

    companion object Factory
}

fun RefreshTokenUseCase.Factory.build(
    authenticationRepository: AuthenticationRepository,
): RefreshTokenUseCase = RefreshTokenUseCaseImpl(authenticationRepository)

private class RefreshTokenUseCaseImpl(
    private val authenticationRepository: AuthenticationRepository,
) : RefreshTokenUseCase {
    override suspend fun invoke() {
        authenticationRepository.refreshToken()
    }
}