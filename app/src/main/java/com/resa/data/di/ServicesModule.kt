package com.resa.data.di

import com.resa.data.network.services.LocationsService
import com.resa.data.network.services.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object ServicesModule {

    @Singleton
    @Provides
    fun providesLocationsService(): LocationsService {
        return RetrofitService.getInstance(LocationsService::class.java)
    }
}