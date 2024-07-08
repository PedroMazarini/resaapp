package com.mazarini.resa.data.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mazarini.resa.data.cache.entities.RecentJourneySearch
import com.mazarini.resa.data.cache.entities.RecentLocation
import com.mazarini.resa.data.cache.entities.SavedJourneySearch
import com.mazarini.resa.data.cache.entities.SavedLocation

@Database(
    entities = [
        SavedLocation::class,
        RecentLocation::class,
        SavedJourneySearch::class,
        RecentJourneySearch::class,
    ],
    version = 5,
    exportSchema = false
)
abstract class ResaDatabase : RoomDatabase() {

    abstract fun savedJourneySearchDao(): SavedJourneySearchDao

    abstract fun recentJourneySearchDao(): RecentJourneySearchDao

    abstract fun savedLocationDao(): SavedLocationDao

    abstract fun recentLocationDao(): RecentLocationDao

    companion object {
        const val DATABASE_NAME = "resa_db"
    }
}