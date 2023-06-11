package com.resa.domain.usecases

import com.resa.data.repository.AuthenticationRepository
import javax.inject.Inject

interface RefreshTokenUseCase {
    suspend operator fun invoke()
}

class RefreshTokenUseCaseImpl
@Inject
constructor(
    private val authenticationRepository: AuthenticationRepository
) : RefreshTokenUseCase {
    override suspend fun invoke() {
        authenticationRepository.refreshToken()
    }
}