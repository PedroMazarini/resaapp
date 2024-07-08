package com.mazarini.resa.domain.di

import com.mazarini.resa.data.repository.AuthenticationRepository
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository
import com.mazarini.resa.domain.usecases.RefreshTokenUseCase
import com.mazarini.resa.domain.usecases.RefreshTokenUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.CheckHasVehiclePositionUseCase
import com.mazarini.resa.domain.usecases.journey.CheckHasVehiclePositionUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.DeleteRecentJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.DeleteRecentJourneySearchUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.DeleteSavedJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.DeleteSavedJourneySearchUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.FetchSelectedJourneyDetailsUseCase
import com.mazarini.resa.domain.usecases.journey.FetchSelectedJourneyDetailsUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.GetCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.GetCurrentJourneyQueryUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.GetRecentJourneySearchesUseCase
import com.mazarini.resa.domain.usecases.journey.GetRecentJourneySearchesUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.GetSavedJourneySearchesUseCase
import com.mazarini.resa.domain.usecases.journey.GetSavedJourneySearchesUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.GetSelectedJourneyUseCase
import com.mazarini.resa.domain.usecases.journey.GetSelectedJourneyUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.ListenVehiclePositionUseCase
import com.mazarini.resa.domain.usecases.journey.ListenVehiclePositionUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.LoadSavedJourneyToHomeUseCase
import com.mazarini.resa.domain.usecases.journey.LoadSavedJourneyToHomeUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.QueryJourneysUseCase
import com.mazarini.resa.domain.usecases.journey.QueryJourneysUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.QueryPassedJourneysUseCase
import com.mazarini.resa.domain.usecases.journey.QueryPassedJourneysUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyToHomeUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyToHomeUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.SaveJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.SaveJourneySearchUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.SaveRecentJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.SaveRecentJourneySearchUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.SetSelectedJourneyUseCase
import com.mazarini.resa.domain.usecases.journey.SetSelectedJourneyUseCaseImpl
import com.mazarini.resa.domain.usecases.journey.StartVehicleTrackingUseCase
import com.mazarini.resa.domain.usecases.journey.StartVehicleTrackingUseCaseImpl
import com.mazarini.resa.domain.usecases.location.DeleteSavedLocationUseCase
import com.mazarini.resa.domain.usecases.location.DeleteSavedLocationUseCaseImpl
import com.mazarini.resa.domain.usecases.location.GetRecentLocationsUseCase
import com.mazarini.resa.domain.usecases.location.GetRecentLocationsUseCaseImpl
import com.mazarini.resa.domain.usecases.location.GetSavedLocationsUseCase
import com.mazarini.resa.domain.usecases.location.GetSavedLocationsUseCaseImpl
import com.mazarini.resa.domain.usecases.location.QueryLocationByTextUseCase
import com.mazarini.resa.domain.usecases.location.QueryLocationByTextUseCaseImpl
import com.mazarini.resa.domain.usecases.location.SaveLocationUseCase
import com.mazarini.resa.domain.usecases.location.SaveLocationUseCaseImpl
import com.mazarini.resa.domain.usecases.location.SaveRecentLocationUseCase
import com.mazarini.resa.domain.usecases.location.SaveRecentLocationUseCaseImpl
import com.mazarini.resa.domain.usecases.stoparea.GetStopDeparturesUseCase
import com.mazarini.resa.domain.usecases.stoparea.GetStopDeparturesUseCaseImpl
import com.mazarini.resa.domain.usecases.stoparea.GetStopsByCoordinateUseCase
import com.mazarini.resa.domain.usecases.stoparea.GetStopsByCoordinateUseCaseImpl
import com.mazarini.resa.domain.usecases.stoparea.GetStopsByNameUseCase
import com.mazarini.resa.domain.usecases.stoparea.GetStopsByNameUseCaseImpl
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
    fun providesCheckHasVehiclePositionUseCase(
        journeysRepository: JourneysRepository,
    ): CheckHasVehiclePositionUseCase {
        return CheckHasVehiclePositionUseCaseImpl(
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
    fun providesSaveCurrentJourneyToHomeUseCase(
        journeysRepository: JourneysRepository,
    ): SaveCurrentJourneyToHomeUseCase {
        return SaveCurrentJourneyToHomeUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesLoadSavedJourneyToHomeUseCase(
        journeysRepository: JourneysRepository,
    ): LoadSavedJourneyToHomeUseCase {
        return LoadSavedJourneyToHomeUseCaseImpl(
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
        stopsRepository: StopsRepository,
    ): GetStopsByCoordinateUseCase {
        return GetStopsByCoordinateUseCaseImpl(
            stopsRepository = stopsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetStopsByNameUseCase(
        stopsRepository: StopsRepository,
    ): GetStopsByNameUseCase {
        return GetStopsByNameUseCaseImpl(
            stopsRepository = stopsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetStopDeparturesUseCase(
        stopsRepository: StopsRepository,
    ): GetStopDeparturesUseCase {
        return GetStopDeparturesUseCaseImpl(
            stopsRepository = stopsRepository,
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
    fun providesDeleteRecentJourneySearchUseCase(
        journeysRepository: JourneysRepository,
    ): DeleteRecentJourneySearchUseCase {
        return DeleteRecentJourneySearchUseCaseImpl(
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

    @Singleton
    @Provides
    fun providesListenVehiclePositionUseCase(
        journeysRepository: JourneysRepository,
    ): ListenVehiclePositionUseCase {
        return ListenVehiclePositionUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesStartVehicleTrackingUseCase(
        journeysRepository: JourneysRepository,
    ): StartVehicleTrackingUseCase {
        return StartVehicleTrackingUseCaseImpl(
            journeysRepository = journeysRepository,
        )
    }
}
