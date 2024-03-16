package com.resa.domain.di

import com.resa.data.repository.AuthenticationRepository
import com.resa.domain.repositoryAbstraction.JourneysRepository
import com.resa.domain.repositoryAbstraction.LocationsRepository
import com.resa.domain.repositoryAbstraction.StopPointsRepository
import com.resa.domain.usecases.RefreshTokenUseCase
import com.resa.domain.usecases.RefreshTokenUseCaseImpl
import com.resa.domain.usecases.journey.DeleteSavedJourneySearchUseCase
import com.resa.domain.usecases.journey.DeleteSavedJourneySearchUseCaseImpl
import com.resa.domain.usecases.journey.FetchSelectedJourneyDetailsUseCase
import com.resa.domain.usecases.journey.FetchSelectedJourneyDetailsUseCaseImpl
import com.resa.domain.usecases.journey.GetCurrentJourneyQueryUseCase
import com.resa.domain.usecases.journey.GetCurrentJourneyQueryUseCaseImpl
import com.resa.domain.usecases.journey.GetRecentJourneySearchesUseCase
import com.resa.domain.usecases.journey.GetRecentJourneySearchesUseCaseImpl
import com.resa.domain.usecases.journey.GetSavedJourneySearchesUseCase
import com.resa.domain.usecases.journey.GetSavedJourneySearchesUseCaseImpl
import com.resa.domain.usecases.journey.GetSelectedJourneyUseCase
import com.resa.domain.usecases.journey.GetSelectedJourneyUseCaseImpl
import com.resa.domain.usecases.journey.QueryJourneysUseCase
import com.resa.domain.usecases.journey.QueryJourneysUseCaseImpl
import com.resa.domain.usecases.journey.QueryPassedJourneysUseCase
import com.resa.domain.usecases.journey.QueryPassedJourneysUseCaseImpl
import com.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCaseImpl
import com.resa.domain.usecases.journey.SaveJourneySearchUseCase
import com.resa.domain.usecases.journey.SaveJourneySearchUseCaseImpl
import com.resa.domain.usecases.journey.SaveRecentJourneySearchUseCase
import com.resa.domain.usecases.journey.SaveRecentJourneySearchUseCaseImpl
import com.resa.domain.usecases.journey.SetSelectedJourneyUseCase
import com.resa.domain.usecases.journey.SetSelectedJourneyUseCaseImpl
import com.resa.domain.usecases.location.DeleteSavedLocationUseCase
import com.resa.domain.usecases.location.DeleteSavedLocationUseCaseImpl
import com.resa.domain.usecases.location.GetRecentLocationsUseCase
import com.resa.domain.usecases.location.GetRecentLocationsUseCaseImpl
import com.resa.domain.usecases.location.GetSavedLocationsUseCase
import com.resa.domain.usecases.location.GetSavedLocationsUseCaseImpl
import com.resa.domain.usecases.location.QueryLocationByTextUseCase
import com.resa.domain.usecases.location.QueryLocationByTextUseCaseImpl
import com.resa.domain.usecases.location.SaveLocationUseCase
import com.resa.domain.usecases.location.SaveLocationUseCaseImpl
import com.resa.domain.usecases.location.SaveRecentLocationUseCase
import com.resa.domain.usecases.location.SaveRecentLocationUseCaseImpl
import com.resa.domain.usecases.stoparea.GetDeparturesAroundUseCase
import com.resa.domain.usecases.stoparea.GetDeparturesAroundUseCaseImpl
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
    fun providesQueryPassedJourneysUseCase(
        journeysRepository: JourneysRepository,
    ): QueryPassedJourneysUseCase {
        return QueryPassedJourneysUseCaseImpl(
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

    @Singleton
    @Provides
    fun providesGetCurrentJourneyQueryUseCase(
        journeysRepository: JourneysRepository,
    ): GetCurrentJourneyQueryUseCase {
        return GetCurrentJourneyQueryUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSaveLocationUseCase(
        locationsRepository: LocationsRepository,
    ): SaveLocationUseCase {
        return SaveLocationUseCaseImpl(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesDeleteLocationUseCase(
        locationsRepository: LocationsRepository,
    ): DeleteSavedLocationUseCase {
        return DeleteSavedLocationUseCaseImpl(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetSavedLocationsUseCase(
        locationsRepository: LocationsRepository,
    ): GetSavedLocationsUseCase {
        return GetSavedLocationsUseCaseImpl(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSaveRecentLocationUseCase(
        locationsRepository: LocationsRepository,
    ): SaveRecentLocationUseCase {
        return SaveRecentLocationUseCaseImpl(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetRecentLocationsUseCase(
        locationsRepository: LocationsRepository,
    ): GetRecentLocationsUseCase {
        return GetRecentLocationsUseCaseImpl(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetRecentJourneySearchesUseCase(
        journeysRepository: JourneysRepository,
    ): GetRecentJourneySearchesUseCase {
        return GetRecentJourneySearchesUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSaveRecentJourneySearchUseCase(
        journeysRepository: JourneysRepository,
    ): SaveRecentJourneySearchUseCase {
        return SaveRecentJourneySearchUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetSavedJourneySearchesUseCase(
        journeysRepository: JourneysRepository,
    ): GetSavedJourneySearchesUseCase {
        return GetSavedJourneySearchesUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesDeleteSavedJourneySearchUseCase(
        journeysRepository: JourneysRepository,
    ): DeleteSavedJourneySearchUseCase {
        return DeleteSavedJourneySearchUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSaveJourneySearchUseCase(
        journeysRepository: JourneysRepository,
    ): SaveJourneySearchUseCase {
        return SaveJourneySearchUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetDeparturesAroundUseCase(
        stopPointsRepository: StopPointsRepository,
    ): GetDeparturesAroundUseCase {
        return GetDeparturesAroundUseCaseImpl(
            stopPointsRepository = stopPointsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesFetchSelectedJourneyDetailsUseCase(
        journeysRepository: JourneysRepository,
    ): FetchSelectedJourneyDetailsUseCase {
        return FetchSelectedJourneyDetailsUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetSelectedJourneyUseCase(
        journeysRepository: JourneysRepository,
    ): GetSelectedJourneyUseCase {
        return GetSelectedJourneyUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }
    @Singleton
    @Provides
    fun providesSetSelectedJourneyUseCase(
        journeysRepository: JourneysRepository,
    ): SetSelectedJourneyUseCase {
        return SetSelectedJourneyUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }
}
