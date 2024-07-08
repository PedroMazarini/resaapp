package com.mazarini.resa.domain.usecases

import com.mazarini.resa.data.repository.AuthenticationRepository
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