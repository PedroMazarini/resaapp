package com.resa.domain.model.journey

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import com.resa.ui.theme.colors.AtomicTangerine
import com.resa.ui.theme.colors.Mahogany
import com.resa.ui.theme.colors.TangerineYellow

sealed class WarningTypes {

    object NoWarning : WarningTypes()
    object LowWarning : WarningTypes()
    object MediumWarning : WarningTypes()
    object HighWarning : WarningTypes()

    fun color(): Color = when (this) {
        is LowWarning -> TangerineYellow
        is MediumWarning -> AtomicTangerine
        is HighWarning -> Mahogany
        else -> White
    }
}
