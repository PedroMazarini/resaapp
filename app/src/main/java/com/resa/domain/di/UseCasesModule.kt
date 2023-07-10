package com.resa.domain.di

import com.resa.data.repository.AuthenticationRepository
import com.resa.data.repository.JourneysRepositoryImpl
import com.resa.domain.repositoryAbstraction.JourneysRepository
import com.resa.domain.repositoryAbstraction.LocationsRepository
import com.resa.domain.usecases.QueryJourneysUseCase
import com.resa.domain.usecases.QueryJourneysUseCaseImpl
import com.resa.domain.usecases.QueryLocationByTextUseCase
import com.resa.domain.usecases.QueryLocationByTextUseCaseImpl
import com.resa.domain.usecases.RefreshTokenUseCase
import com.resa.domain.usecases.RefreshTokenUseCaseImpl
import com.resa.domain.usecases.SaveCurrentJourneyQueryUseCase
import com.resa.domain.usecases.SaveCurrentJourneyQueryUseCaseImpl
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
    ): QueryLocationByTextUseCase {
        return QueryLocationByTextUseCaseImpl(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSaveCurrentJourneyQueryUseCase(
        journeysRepository: JourneysRepository,
    ): SaveCurrentJourneyQueryUseCase {
        return SaveCurrentJourneyQueryUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }
}
