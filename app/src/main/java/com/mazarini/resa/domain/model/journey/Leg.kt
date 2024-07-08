package com.mazarini.resa.domain.model.journey

import androidx.compose.ui.graphics.Color
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.ui.theme.colors.FrenchBlue
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
data class Leg (
    val index: Int,
    val legType: LegType,
    val name: String,
    val durationInMinutes: Int,
    val transportMode: TransportMode,
    val distanceInMeters: Int,
    val warnings: List<Warning>,
    val departTime: JourneyTimes,
    val direction: Direction?,
    val directionName: String,
    val details: LegDetails?,
    val colors: LegColors = LegColors(
        foreground = Color.White,
        background = FrenchBlue,
        border = FrenchBlue,
    ),
)
