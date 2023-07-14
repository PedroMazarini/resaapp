package com.resa.ui.di

import com.resa.ui.screens.mapper.DomainToUiJourneySearchMapper
import com.resa.ui.screens.mapper.DomainToUiLocationMapper
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
    fun providesDomainToUiLocationMapper(): DomainToUiLocationMapper {
        return DomainToUiLocationMapper()
    }

    @Singleton
    @Provides
    fun providesDomainToUiJourneySearchMapper(
        locationMapper: DomainToUiLocationMapper
    ): DomainToUiJourneySearchMapper {
        return DomainToUiJourneySearchMapper(locationMapper = locationMapper)
    }
}
