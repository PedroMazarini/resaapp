package com.mazarini.resa.data.di

import com.mazarini.resa.data.network.datasource.AuthenticationDatasourceImpl
import com.mazarini.resa.data.network.datasource.JourneysDatasourceImpl
import com.mazarini.resa.data.network.datasource.LocationsDatasourceImpl
import com.mazarini.resa.data.network.datasource.StopsDatasourceImpl
import com.mazarini.resa.data.network.datasource.abstraction.AuthenticationDatasource
import com.mazarini.resa.data.network.datasource.abstraction.JourneysDatasource
import com.mazarini.resa.data.network.datasource.abstraction.LocationsDatasource
import com.mazarini.resa.data.network.datasource.abstraction.StopsDatasource
import com.mazarini.resa.data.network.mappers.QueryJourneysParamsMapper
import com.mazarini.resa.data.network.mappers.QueryLocationsParamsMapper
import com.mazarini.resa.data.network.mappers.RemoteToDomainJourneyMapper
import com.mazarini.resa.data.network.mappers.RemoteToDomainLegDetailsMapper
import com.mazarini.resa.data.network.mappers.RemoteToDomainLocationMapper
import com.mazarini.resa.data.network.mappers.RemoteToDomainStopJourneyMapper
import com.mazarini.resa.data.network.mappers.RemoteToDomainVehicleMapper
import com.mazarini.resa.data.network.services.RetrofitService
import com.mazarini.resa.data.network.services.RetrofitService.BASE_URL_TRAVEL_PLANNER
import com.mazarini.resa.data.network.services.travelplanner.JourneysService
import com.mazarini.resa.data.network.services.travelplanner.LocationsService
import com.mazarini.resa.data.network.services.travelplanner.StopDeparturesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourcesModule {

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
        remoteToDomainLegDetailsMapper: RemoteToDomainLegDetailsMapper,
        remoteToDomainVehicleMapper: RemoteToDomainVehicleMapper,
        queryJourneysParamsMapper: QueryJourneysParamsMapper,
    ): JourneysDatasource {
        return JourneysDatasourceImpl(
            remoteToDomainJourneyMapper = remoteToDomainJourneyMapper,
            remoteToDomainLegDetailsMapper = remoteToDomainLegDetailsMapper,
            remoteToDomainVehicleMapper = remoteToDomainVehicleMapper,
            journeysService = RetrofitService.getInstance(
                JourneysService::class.java,
                baseUrl = BASE_URL_TRAVEL_PLANNER,
            ),
            mapper = queryJourneysParamsMapper,
        )
    }

    @Singleton
    @Provides
    fun providesLocationsDatasource(
        remoteToDomainLocationMapper: RemoteToDomainLocationMapper,
        queryLocationsParamsMapper: QueryLocationsParamsMapper,
    ): LocationsDatasource {
        return LocationsDatasourceImpl(
            remoteToDomainLocationMapper = remoteToDomainLocationMapper,
            locationsService = RetrofitService.getInstance(
                LocationsService::class.java,
                baseUrl = BASE_URL_TRAVEL_PLANNER,
            ),
            mapper = queryLocationsParamsMapper,
        )
    }

    @Singleton
    @Provides
    fun providesStopPointsDatasource(
        paramsMapper: QueryLocationsParamsMapper,
        locationMapper: RemoteToDomainLocationMapper,
        stopJourneyMapper: RemoteToDomainStopJourneyMapper,
    ): StopsDatasource {
        return StopsDatasourceImpl(
            paramsMapper = paramsMapper,
            locationMapper = locationMapper,
            stopJourneyMapper = stopJourneyMapper,
            locationsService = RetrofitService.getInstance(LocationsService::class.java),
            stopDeparturesService = RetrofitService.getInstance(StopDeparturesService::class.java),
        )
    }
}
