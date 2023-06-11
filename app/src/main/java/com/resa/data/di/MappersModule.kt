package com.resa.data.di

import com.resa.data.network.mappers.QueryJourneysParamsMapper
import com.resa.data.network.mappers.QueryLocationsParamsMapper
import com.resa.data.network.mappers.RemoteJourneyToDomainJourneyMapper
import com.resa.data.network.mappers.RemoteJourneysResponseToDomain
import com.resa.data.network.mappers.RemoteLocationToDomain
import com.resa.data.network.mappers.RemoteLocationsResponseToDomain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MappersModule {

    @Singleton
    @Provides
    fun providesQueryJourneysParamsToQueryMapMapper(): QueryJourneysParamsMapper {
        return QueryJourneysParamsMapper()
    }

    @Singleton
    @Provides
    fun providesRemoteJourneysResponseToDomain(
        remoteJourneyToDomainJourneyMapper: RemoteJourneyToDomainJourneyMapper,
    ): RemoteJourneysResponseToDomain {
        return RemoteJourneysResponseToDomain(
            remoteJourneyToDomainJourneyMapper = remoteJourneyToDomainJourneyMapper,
        )
    }

    @Singleton
    @Provides
    fun providesRemoteJourneyToDomainJourneyMapper(): RemoteJourneyToDomainJourneyMapper {
        return RemoteJourneyToDomainJourneyMapper()
    }

    @Singleton
    @Provides
    fun providesQueryLocationsParamsMapper(): QueryLocationsParamsMapper {
        return QueryLocationsParamsMapper()
    }

    @Singleton
    @Provides
    fun providesRemoteLocationsResponseToDomain(
        remoteLocationToDomain: RemoteLocationToDomain,
    ): RemoteLocationsResponseToDomain {
        return RemoteLocationsResponseToDomain(
            remoteLocationToDomain = remoteLocationToDomain,
        )
    }

    @Singleton
    @Provides
    fun providesRemoteLocationToDomain(): RemoteLocationToDomain {
        return RemoteLocationToDomain()
    }
}