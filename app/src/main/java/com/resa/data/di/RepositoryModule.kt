package com.resa.data.di

import com.resa.data.cache.service.RecentJourneySearchCacheService
import com.resa.data.cache.service.RecentLocationCacheService
import com.resa.data.cache.service.SavedJourneySearchCacheService
import com.resa.data.cache.service.SavedLocationCacheService
import com.resa.data.network.datasource.abstraction.JourneysDatasource
import com.resa.data.network.datasource.abstraction.LocationsDatasource
import com.resa.data.repository.JourneysRepositoryImpl
import com.resa.data.repository.LocationsRepositoryImpl
import com.resa.domain.repositoryAbstraction.JourneysRepository
import com.resa.domain.repositoryAbstraction.LocationsRepository
import com.resa.global.preferences.PrefsProvider
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
}
