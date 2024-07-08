package com.mazarini.resa.data.di

import android.content.Context
import androidx.room.Room
import com.mazarini.resa.data.cache.database.RecentJourneySearchDao
import com.mazarini.resa.data.cache.database.RecentLocationDao
import com.mazarini.resa.data.cache.database.ResaDatabase
import com.mazarini.resa.data.cache.database.ResaDatabase.Companion.DATABASE_NAME
import com.mazarini.resa.data.cache.database.SavedJourneySearchDao
import com.mazarini.resa.data.cache.database.SavedLocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class CacheModule {

    @Singleton
    @Provides
    fun provide(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, ResaDatabase::class.java, DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideSavedLocationDao(database: ResaDatabase): SavedLocationDao {
        return database.savedLocationDao()
    }

    @Singleton
    @Provides
    fun provideRecentLocationDao(database: ResaDatabase): RecentLocationDao {
        return database.recentLocationDao()
    }

    @Singleton
    @Provides
    fun provideRecentJourneySearchDao(database: ResaDatabase): RecentJourneySearchDao {
        return database.recentJourneySearchDao()
    }

    @Singleton
    @Provides
    fun provideSavedJourneySearchDao(database: ResaDatabase): SavedJourneySearchDao {
        return database.savedJourneySearchDao()
    }
}