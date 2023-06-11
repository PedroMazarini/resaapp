package com.resa.data.di

import com.resa.data.network.datasource.abstraction.AuthenticationDatasource
import com.resa.data.network.datasource.AuthenticationDatasourceImpl
import com.resa.data.network.datasource.abstraction.JourneysDatasource
import com.resa.data.network.datasource.JourneysDatasourceImpl
import com.resa.data.network.datasource.LocationsDatasourceImpl
import com.resa.data.network.datasource.abstraction.LocationsDatasource
import com.resa.data.network.mappers.QueryJourneysParamsMapper
import com.resa.data.network.mappers.QueryLocationsParamsMapper
import com.resa.data.network.mappers.RemoteJourneysResponseToDomain
import com.resa.data.network.mappers.RemoteLocationsResponseToDomain
import com.resa.data.network.services.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatasourcesModule {

    @Singleton
    @Provides
    fun providesAuthenticationDatasource(): AuthenticationDatasource {
        return AuthenticationDatasourceImpl(
            retrofitService = RetrofitService,
        )
    }

    @Singleton
    @Provides
    fun providesJourneysDatasource(
        queryJourneysParamsMapper: QueryJourneysParamsMapper,
        remoteJourneysResponseToDomain: RemoteJourneysResponseToDomain,
    ): JourneysDatasource {
        return JourneysDatasourceImpl(
            retrofitService = RetrofitService,
            journeysParamsMapper = queryJourneysParamsMapper,
            remoteJourneysResponseToDomain = remoteJourneysResponseToDomain,
        )
    }

    @Singleton
    @Provides
    fun providesLocationsDatasource(
        queryLocationsParamsMapper: QueryLocationsParamsMapper,
        remoteLocationsResponseToDomain: RemoteLocationsResponseToDomain,
    ): LocationsDatasource {
        return LocationsDatasourceImpl(
            retrofitService = RetrofitService,
            locationsParamsMapper = queryLocationsParamsMapper,
            remoteLocationsResponseToDomain = remoteLocationsResponseToDomain,
        )
    }
}
