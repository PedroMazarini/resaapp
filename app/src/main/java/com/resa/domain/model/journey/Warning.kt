package com.resa.domain.model.journey

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.colors.AtomicTangerine
import com.resa.ui.theme.colors.Mahogany
import com.resa.ui.theme.colors.TangerineYellow

data class Warning(
    val severity: WarningSeverity,
    val message: String,
)

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