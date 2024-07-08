package com.mazarini.resa.data.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.mazarini.resa.domain.model.LocationType
import kotlinx.coroutines.flow.Flow
import com.mazarini.resa.data.cache.entities.RecentLocation as CacheLocation

@Dao
interface RecentLocationDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertLocation(location: CacheLocation): Long

    @Query("SELECT * FROM recent_locations WHERE type IN (:types) ORDER BY updatedAt DESC LIMIT :limit")
    fun getAllLocations(
        limit: Int,
        types: List<LocationType>,
    ): Flow<List<CacheLocation>>

    @Query("DELETE FROM recent_locations where id NOT IN " +
            "(SELECT id from recent_locations ORDER BY updatedAt DESC LIMIT 100)")
    fun clearOldData()
}
