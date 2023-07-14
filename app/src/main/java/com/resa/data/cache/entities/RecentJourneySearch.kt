package com.resa.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.resa.domain.model.LocationType
import java.util.Date

@Entity(tableName = "recent_journey_search")
data class RecentJourneySearch(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "originId")
    var originId: String,
    @ColumnInfo(name = "originName")
    var originName: String,
    @ColumnInfo(name = "originLat")
    var originLat: Double?,
    @ColumnInfo(name = "originLon")
    var originLon: Double?,
    @ColumnInfo(name = "originType")
    var originType: LocationType,

    @ColumnInfo(name = "destinationId")
    var destinationId: String,
    @ColumnInfo(name = "destinationName")
    var destinationName: String,
    @ColumnInfo(name = "destinationLat")
    var destinationLat: Double?,
    @ColumnInfo(name = "destinationLon")
    var destinationLon: Double?,
    @ColumnInfo(name = "destinationType")
    var destinationType: LocationType,




)
