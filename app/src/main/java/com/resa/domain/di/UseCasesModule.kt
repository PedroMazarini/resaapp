package com.resa.domain.di

import com.resa.data.repository.AuthenticationRepository
import com.resa.data.repository.JourneysRepository
import com.resa.data.repository.LocationsRepository
import com.resa.domain.usecases.QueryJourneysUseCase
import com.resa.domain.usecases.QueryJourneysUseCaseImpl
import com.resa.domain.usecases.QueryLocationsUseCase
import com.resa.domain.usecases.QueryLocationsUseCaseImpl
import com.resa.domain.usecases.RefreshTokenUseCase
import com.resa.domain.usecases.RefreshTokenUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Singleton
    @Provides
    fun providesRefreshTokenUsecase(
        authenticationRepository: AuthenticationRepository,
    ): RefreshTokenUseCase {
        return RefreshTokenUseCaseImpl(
            authenticationRepository = authenticationRepository,
        )
    }

    @Singleton
    @Provides
    fun providesQueryJourneysUseCase(
        journeysRepository: JourneysRepository,
    ): QueryJourneysUseCase {
        return QueryJourneysUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesQueryLocationsUseCase(
        locationsRepository: LocationsRepository,
    ): QueryLocationsUseCase {
        return QueryLocationsUseCaseImpl(
            locationsRepository = locationsRepository,
        )
    }
}
