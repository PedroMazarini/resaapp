package com.resa.data.di

import com.resa.data.network.datasource.abstraction.AuthenticationDatasource
import com.resa.data.network.datasource.AuthenticationDatasourceImpl
import com.resa.data.network.datasource.abstraction.JourneysDatasource
import com.resa.data.network.datasource.JourneysDatasourceImpl
import com.resa.data.network.datasource.LocationsDatasourceImpl
import com.resa.data.network.datasource.StopPointsDatasourceImpl
import com.resa.data.network.datasource.abstraction.LocationsDatasource
import com.resa.data.network.datasource.abstraction.StopPointsDatasource
import com.resa.data.network.mappers.RemoteToDomainLocationMapper
import com.resa.data.network.mappers.RemoteToDomainJourneyMapper
import com.resa.data.network.mappers.RemoteToDomainStopPointsMapper
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
        remoteToDomainJourneyMapper: RemoteToDomainJourneyMapper,
    ): JourneysDatasource {
        return JourneysDatasourceImpl(
            remoteToDomainJourneyMapper = remoteToDomainJourneyMapper,
        )
    }

    @Singleton
    @Provides
    fun providesLocationsDatasource(
        remoteToDomainLocationMapper: RemoteToDomainLocationMapper,
    ): LocationsDatasource {
        return LocationsDatasourceImpl(
            remoteToDomainLocationMapper = remoteToDomainLocationMapper,
        )
    }

    @Singleton
    @Provides
    fun providesStopPointsDatasource(
        remoteToDomainStopPointsMapper: RemoteToDomainStopPointsMapper,
    ): StopPointsDatasource {
        return StopPointsDatasourceImpl(
            remoteToDomainStopPointsMapper = remoteToDomainStopPointsMapper,
        )
    }
}
