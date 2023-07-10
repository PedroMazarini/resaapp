package com.resa.data.di

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
    ): LocationsRepository {
        return LocationsRepositoryImpl(
            locationsDatasource = locationsDatasource,
            prefsProvider = prefsProvider,
        )
    }

    @Singleton
    @Provides
    fun providesJourneysRepository(
        journeysDatasource: JourneysDatasource,
        prefsProvider: PrefsProvider,
    ): JourneysRepository {
        return JourneysRepositoryImpl(
            journeysDatasource = journeysDatasource,
            prefsProvider = prefsProvider,
        )
    }
}
