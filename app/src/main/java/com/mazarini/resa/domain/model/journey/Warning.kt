package com.mazarini.resa.domain.model.journey

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mazarini.resa.ui.theme.MTheme
import kotlinx.serialization.Serializable

@Serializable
data class Warning(
    val severity: WarningSeverity,
    val message: String,
)

val Warning.filteredMessage: String
    get() = message
        .replace("Sök din resa i appen To Go eller på vasttrafik.se. ", "")
        .replace("Sök din resa i appen To Go. ", "")

enum class WarningSeverity {
    LOW, MEDIUM, HIGH;

    @Composable
    fun color(): Color = when (this) {
        LOW -> MTheme.colors.warningLow
        MEDIUM -> MTheme.colors.warningMedium
        HIGH -> MTheme.colors.warningHigh
    }

    @Composable
    fun bgColor(): Color = when (this) {
        LOW -> MTheme.colors.warningLowBg
        MEDIUM -> MTheme.colors.warningMediumBg
        HIGH -> MTheme.colors.warningHighBg
    }
}