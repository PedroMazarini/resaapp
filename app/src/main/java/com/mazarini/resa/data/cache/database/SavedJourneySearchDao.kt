package com.mazarini.resa.data.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.mazarini.resa.data.cache.entities.SavedJourneySearch as CacheSavedJourneySearch

@Dao
interface SavedJourneySearchDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertJourney(journey: CacheSavedJourneySearch): Long

    @Query("SELECT * FROM saved_journey_search ORDER BY lastModified DESC")
    fun getAllSavedJourneySearch(): Flow<List<CacheSavedJourneySearch>>

    @Query("DELETE FROM saved_journey_search WHERE id = :id")
    fun deleteJourneyById(id: String)

    @Query("DELETE FROM saved_journey_search where id NOT IN " +
            "(SELECT id from saved_journey_search ORDER BY lastModified DESC LIMIT 50)")
    fun clearOldData()
}
