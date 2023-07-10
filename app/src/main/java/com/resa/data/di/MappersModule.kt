package com.resa.data.di

import com.resa.data.network.mappers.QueryJourneysParamsMapper
import com.resa.data.network.mappers.QueryLocationsParamsMapper
import com.resa.data.network.mappers.RemoteArrivalLinkToDomainLegMapper
import com.resa.data.network.mappers.RemoteDepartLinkToDomainLegMapper
import com.resa.data.network.mappers.RemoteLinkToDomainLegMapper
import com.resa.data.network.mappers.RemoteToDomainJourneyMapper
import com.resa.data.network.mappers.RemoteToDomainLegMapper
import com.resa.data.network.mappers.RemoteToDomainLocationMapper
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
    fun providesRemoteToDomainJourneyMapper(
        remoteToDomainLegMapper: RemoteToDomainLegMapper,
        remoteLinkToDomainLegMapper: RemoteLinkToDomainLegMapper,
        remoteDepartLinkToDomainLegMapper: RemoteDepartLinkToDomainLegMapper,
        remoteArrivalLinkToDomainLegMapper: RemoteArrivalLinkToDomainLegMapper,
    ): RemoteToDomainJourneyMapper {
        return RemoteToDomainJourneyMapper(
            legMapper = remoteToDomainLegMapper,
            linkMapper = remoteLinkToDomainLegMapper,
            departLinkMapper = remoteDepartLinkToDomainLegMapper,
            arrivalLinkMapper = remoteArrivalLinkToDomainLegMapper,
        )
    }

    @Singleton
    @Provides
    fun providesQueryLocationsParamsMapper(): QueryLocationsParamsMapper {
        return QueryLocationsParamsMapper()
    }

    @Singleton
    @Provides
    fun providesRemoteLocationToDomain(): RemoteToDomainLocationMapper {
        return RemoteToDomainLocationMapper()
    }

    @Singleton
    @Provides
    fun providesRemoteToDomainLegMapper(): RemoteToDomainLegMapper {
        return RemoteToDomainLegMapper()
    }

    @Singleton
    @Provides
    fun providesRemoteLinkToDomainLegMapper(): RemoteLinkToDomainLegMapper {
        return RemoteLinkToDomainLegMapper()
    }

    @Singleton
    @Provides
    fun providesRemoteDepartLinkToDomainLegMapper(): RemoteDepartLinkToDomainLegMapper {
        return RemoteDepartLinkToDomainLegMapper()
    }

    @Singleton
    @Provides
    fun providesRemoteArrivalLinkToDomainLegMapper(): RemoteArrivalLinkToDomainLegMapper {
        return RemoteArrivalLinkToDomainLegMapper()
    }
}