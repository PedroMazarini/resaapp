package com.mazarini.resa.data.di

import com.mazarini.resa.data.cache.service.RecentJourneySearchCacheService
import com.mazarini.resa.data.cache.service.RecentLocationCacheService
import com.mazarini.resa.data.cache.service.SavedJourneySearchCacheService
import com.mazarini.resa.data.cache.service.SavedLocationCacheService
import com.mazarini.resa.data.network.datasource.abstraction.JourneysDatasource
import com.mazarini.resa.data.network.datasource.abstraction.LocationsDatasource
import com.mazarini.resa.data.network.datasource.abstraction.StopsDatasource
import com.mazarini.resa.data.repository.JourneysRepositoryImpl
import com.mazarini.resa.data.repository.LocationsRepositoryImpl
import com.mazarini.resa.data.repository.StopsRepositoryImpl
import com.mazarini.resa.domain.repositoryAbstraction.JourneysRepository
import com.mazarini.resa.domain.repositoryAbstraction.LocationsRepository
import com.mazarini.resa.domain.repositoryAbstraction.StopsRepository
import com.mazarini.resa.global.preferences.PrefsProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesLocationsRepository(
        locationsDatasource: LocationsDatasource,
        prefsProvider: PrefsProvider,
        savedLocationCacheService: SavedLocationCacheService,
        recentLocationCacheService: RecentLocationCacheService,
    ): LocationsRepository {
        return LocationsRepositoryImpl(
            locationsDatasource = locationsDatasource,
            prefsProvider = prefsProvider,
            savedLocationCacheService = savedLocationCacheService,
            recentLocationCacheService = recentLocationCacheService,
        )
    }

    @Singleton
    @Provides
    fun providesJourneysRepository(
        journeysDatasource: JourneysDatasource,
        prefsProvider: PrefsProvider,
        savedJourneySearchCacheService: SavedJourneySearchCacheService,
        recentJourneySearchCacheService: RecentJourneySearchCacheService
    ): JourneysRepository {
        return JourneysRepositoryImpl(
            journeysDatasource = journeysDatasource,
            prefsProvider = prefsProvider,
            savedJourneySearchService = savedJourneySearchCacheService,
            recentJourneySearchService = recentJourneySearchCacheService,
        )
    }

    @Singleton
    @Provides
    fun providesStopAreaRepository(
        stopsDatasource: StopsDatasource,
        prefsProvider: PrefsProvider,
    ): StopsRepository {
        return StopsRepositoryImpl(
            stopsDatasource = stopsDatasource,
            prefsProvider = prefsProvider,
        )
    }
}
