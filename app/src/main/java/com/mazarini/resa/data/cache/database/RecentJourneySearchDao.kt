package com.mazarini.resa.data.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.mazarini.resa.data.cache.entities.RecentJourneySearch as CacheRecentJourneySearch

@Dao
interface RecentJourneySearchDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertJourney(journey: CacheRecentJourneySearch): Long

    @Query("SELECT * FROM recent_journey_search ORDER BY lastModified DESC")
    fun getAllRecentJourneySearch(): Flow<List<CacheRecentJourneySearch>>

    @Query("DELETE FROM recent_journey_search WHERE id = :id")
    fun deleteJourneyById(id: String)

    @Query("DELETE FROM recent_journey_search where id NOT IN " +
            "(SELECT id from recent_journey_search ORDER BY lastModified DESC LIMIT 50)")
    fun clearOldData()
}
