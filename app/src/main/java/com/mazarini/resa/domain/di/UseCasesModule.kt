package com.mazarini.resa.domain.di

import com.mazarini.resa.data.repository.AuthenticationRepository
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository
import com.mazarini.resa.domain.usecases.RefreshTokenUseCase
import com.mazarini.resa.domain.usecases.build
import com.mazarini.resa.domain.usecases.journey.CheckHasVehiclePositionUseCase
import com.mazarini.resa.domain.usecases.journey.ClearOldJourneySearchesUseCase
import com.mazarini.resa.domain.usecases.journey.DeleteRecentJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.DeleteSavedJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.FetchSelectedJourneyDetailsUseCase
import com.mazarini.resa.domain.usecases.journey.GetCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.GetRecentJourneySearchesUseCase
import com.mazarini.resa.domain.usecases.journey.GetSavedJourneySearchesUseCase
import com.mazarini.resa.domain.usecases.journey.GetSelectedJourneyUseCase
import com.mazarini.resa.domain.usecases.journey.ListenVehiclePositionUseCase
import com.mazarini.resa.domain.usecases.journey.LoadSavedJourneyToHomeUseCase
import com.mazarini.resa.domain.usecases.journey.QueryJourneysUseCase
import com.mazarini.resa.domain.usecases.journey.QueryPassedJourneysUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyToHomeUseCase
import com.mazarini.resa.domain.usecases.journey.SaveJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.SaveRecentJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.SetSelectedJourneyUseCase
import com.mazarini.resa.domain.usecases.journey.StartVehicleTrackingUseCase
import com.mazarini.resa.domain.usecases.journey.build
import com.mazarini.resa.domain.usecases.location.ClearOldLocationsUseCase
import com.mazarini.resa.domain.usecases.location.DeleteSavedLocationUseCase
import com.mazarini.resa.domain.usecases.location.GetRecentLocationsUseCase
import com.mazarini.resa.domain.usecases.location.GetSavedLocationsUseCase
import com.mazarini.resa.domain.usecases.location.QueryLocationByTextUseCase
import com.mazarini.resa.domain.usecases.location.SaveLocationUseCase
import com.mazarini.resa.domain.usecases.location.SaveRecentLocationUseCase
import com.mazarini.resa.domain.usecases.location.build
import com.mazarini.resa.domain.usecases.stoparea.GetStopDeparturesUseCase
import com.mazarini.resa.domain.usecases.stoparea.GetStopsByCoordinateUseCase
import com.mazarini.resa.domain.usecases.stoparea.GetStopsByNameUseCase
import com.mazarini.resa.domain.usecases.stoparea.build
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
        return RefreshTokenUseCase.build(
            authenticationRepository = authenticationRepository,
        )
    }

    @Singleton
    @Provides
    fun providesQueryJourneysUseCase(
        journeysRepository: JourneysRepository,
    ): QueryJourneysUseCase {
        return QueryJourneysUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesQueryPassedJourneysUseCase(
        journeysRepository: JourneysRepository,
    ): QueryPassedJourneysUseCase {
        return QueryPassedJourneysUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesQueryLocationsUseCase(
        locationsRepository: LocationsRepository,
    ): QueryLocationByTextUseCase {
        return QueryLocationByTextUseCase.build(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSaveCurrentJourneyQueryUseCase(
        journeysRepository: JourneysRepository,
    ): SaveCurrentJourneyQueryUseCase {
        return SaveCurrentJourneyQueryUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesCheckHasVehiclePositionUseCase(
        journeysRepository: JourneysRepository,
    ): CheckHasVehiclePositionUseCase {
        return CheckHasVehiclePositionUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetCurrentJourneyQueryUseCase(
        journeysRepository: JourneysRepository,
    ): GetCurrentJourneyQueryUseCase {
        return GetCurrentJourneyQueryUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSaveCurrentJourneyToHomeUseCase(
        journeysRepository: JourneysRepository,
    ): SaveCurrentJourneyToHomeUseCase {
        return SaveCurrentJourneyToHomeUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesLoadSavedJourneyToHomeUseCase(
        journeysRepository: JourneysRepository,
    ): LoadSavedJourneyToHomeUseCase {
        return LoadSavedJourneyToHomeUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSaveLocationUseCase(
        locationsRepository: LocationsRepository,
    ): SaveLocationUseCase {
        return SaveLocationUseCase.build(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesDeleteLocationUseCase(
        locationsRepository: LocationsRepository,
    ): DeleteSavedLocationUseCase {
        return DeleteSavedLocationUseCase.build(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetSavedLocationsUseCase(
        locationsRepository: LocationsRepository,
    ): GetSavedLocationsUseCase {
        return GetSavedLocationsUseCase.build(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSaveRecentLocationUseCase(
        locationsRepository: LocationsRepository,
    ): SaveRecentLocationUseCase {
        return SaveRecentLocationUseCase.build(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetRecentLocationsUseCase(
        locationsRepository: LocationsRepository,
    ): GetRecentLocationsUseCase {
        return GetRecentLocationsUseCase.build(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetRecentJourneySearchesUseCase(
        journeysRepository: JourneysRepository,
    ): GetRecentJourneySearchesUseCase {
        return GetRecentJourneySearchesUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSaveRecentJourneySearchUseCase(
        journeysRepository: JourneysRepository,
    ): SaveRecentJourneySearchUseCase {
        return SaveRecentJourneySearchUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetSavedJourneySearchesUseCase(
        journeysRepository: JourneysRepository,
    ): GetSavedJourneySearchesUseCase {
        return GetSavedJourneySearchesUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesDeleteSavedJourneySearchUseCase(
        journeysRepository: JourneysRepository,
    ): DeleteSavedJourneySearchUseCase {
        return DeleteSavedJourneySearchUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSaveJourneySearchUseCase(
        journeysRepository: JourneysRepository,
    ): SaveJourneySearchUseCase {
        return SaveJourneySearchUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetDeparturesAroundUseCase(
        stopsRepository: StopsRepository,
    ): GetStopsByCoordinateUseCase {
        return GetStopsByCoordinateUseCase.build(
            stopsRepository = stopsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetStopsByNameUseCase(
        stopsRepository: StopsRepository,
    ): GetStopsByNameUseCase {
        return GetStopsByNameUseCase.build(
            stopsRepository = stopsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetStopDeparturesUseCase(
        stopsRepository: StopsRepository,
    ): GetStopDeparturesUseCase {
        return GetStopDeparturesUseCase.build(
            stopsRepository = stopsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesFetchSelectedJourneyDetailsUseCase(
        journeysRepository: JourneysRepository,
    ): FetchSelectedJourneyDetailsUseCase {
        return FetchSelectedJourneyDetailsUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesGetSelectedJourneyUseCase(
        journeysRepository: JourneysRepository,
    ): GetSelectedJourneyUseCase {
        return GetSelectedJourneyUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesDeleteRecentJourneySearchUseCase(
        journeysRepository: JourneysRepository,
    ): DeleteRecentJourneySearchUseCase {
        return DeleteRecentJourneySearchUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesSetSelectedJourneyUseCase(
        journeysRepository: JourneysRepository,
    ): SetSelectedJourneyUseCase {
        return SetSelectedJourneyUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesListenVehiclePositionUseCase(
        journeysRepository: JourneysRepository,
    ): ListenVehiclePositionUseCase {
        return ListenVehiclePositionUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesStartVehicleTrackingUseCase(
        journeysRepository: JourneysRepository,
    ): StartVehicleTrackingUseCase {
        return StartVehicleTrackingUseCase.build(
            journeysRepository = journeysRepository,
        )
    }

    @Singleton
    @Provides
    fun providesClearOldLocationsUseCase(
        locationsRepository: LocationsRepository,
    ): ClearOldLocationsUseCase {
        return ClearOldLocationsUseCase.build(
            locationsRepository = locationsRepository,
        )
    }

    @Singleton
    @Provides
    fun providesClearOldJourneySearchesUseCase(
        journeysRepository: JourneysRepository,
    ): ClearOldJourneySearchesUseCase {
        return ClearOldJourneySearchesUseCase.build(
            journeysRepository = journeysRepository,
        )
    }
}
