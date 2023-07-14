package com.resa.data.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.resa.data.cache.entities.RecentJourneySearch as CacheRecentJourneySearch

@Dao
interface RecentJourneySearchDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertJourney(journey: CacheRecentJourneySearch): Long

    @Query("SELECT * FROM recent_journey_search ORDER BY id DESC")
    fun getAllRecentJourneySearch(): Flow<List<CacheRecentJourneySearch>>

    @Query("DELETE FROM recent_journey_search where id NOT IN " +
            "(SELECT id from recent_journey_search ORDER BY id DESC LIMIT 50)")
    fun clearOldData()
}
