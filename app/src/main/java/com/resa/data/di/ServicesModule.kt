package com.resa.data.di

import com.resa.data.cache.database.RecentJourneySearchDao
import com.resa.data.cache.database.RecentLocationDao
import com.resa.data.cache.database.SavedJourneySearchDao
import com.resa.data.cache.database.SavedLocationDao
import com.resa.data.cache.mappers.DomainToCacheRecentJourneySearchMapper
import com.resa.data.cache.mappers.DomainToCacheRecentLocationMapper
import com.resa.data.cache.mappers.DomainToCacheSavedJourneySearchMapper
import com.resa.data.cache.mappers.DomainToCacheSavedLocationMapper
import com.resa.data.cache.service.RecentJourneySearchCacheService
import com.resa.data.cache.service.RecentJourneySearchCacheServiceImpl
import com.resa.data.cache.service.RecentLocationCacheService
import com.resa.data.cache.service.RecentLocationCacheServiceImpl
import com.resa.data.cache.service.SavedJourneySearchCacheService
import com.resa.data.cache.service.SavedJourneySearchCacheServiceImpl
import com.resa.data.cache.service.SavedLocationCacheService
import com.resa.data.cache.service.SavedLocationCacheServiceImpl
import com.resa.data.network.services.LocationsService
import com.resa.data.network.services.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {

    @Singleton
    @Provides
    fun providesLocationsService(): LocationsService {
        return RetrofitService.getInstance(LocationsService::class.java)
    }

    @Singleton
    @Provides
    fun providesSavedLocationCacheService(
        savedLocationDao: SavedLocationDao,
        locationMapper: DomainToCacheSavedLocationMapper,
    ): SavedLocationCacheService {
        return SavedLocationCacheServiceImpl(
            savedLocationDao = savedLocationDao,
            locationMapper = locationMapper,
        )
    }

    @Singleton
    @Provides
    fun providesRecentLocationCacheService(
        recentLocationDao: RecentLocationDao,
        locationMapper: DomainToCacheRecentLocationMapper,
    ): RecentLocationCacheService {
        return RecentLocationCacheServiceImpl(
            recentLocationDao = recentLocationDao,
            locationMapper = locationMapper,
        )
    }

    @Singleton
    @Provides
    fun providesSavedJourneySearchCacheService(
        savedJourneySearchDao: SavedJourneySearchDao,
        journeySearchMapper: DomainToCacheSavedJourneySearchMapper,
    ): SavedJourneySearchCacheService {
        return SavedJourneySearchCacheServiceImpl(
            savedJourneySearchDao = savedJourneySearchDao,
            savedJourneyMapper = journeySearchMapper,
        )
    }

    @Singleton
    @Provides
    fun providesRecentJourneySearchCacheService(
        recentJourneySearchDao: RecentJourneySearchDao,
        recentJourneySearchMapper: DomainToCacheRecentJourneySearchMapper,
    ): RecentJourneySearchCacheService {
        return RecentJourneySearchCacheServiceImpl(
            recentJourneySearchDao = recentJourneySearchDao,
            recentJourneyMapper = recentJourneySearchMapper,
        )
    }
}
