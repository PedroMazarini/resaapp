package com.mazarini.resa.domain.model.journey

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import com.mazarini.resa.ui.theme.colors.AtomicTangerine
import com.mazarini.resa.ui.theme.colors.Mahogany
import com.mazarini.resa.ui.theme.colors.TangerineYellow
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
sealed class WarningTypes {

    @Serializable
    object NoWarning : WarningTypes()
    @Serializable
    object LowWarning : WarningTypes()
    @Serializable
    object MediumWarning : WarningTypes()
    @Serializable
    object HighWarning : WarningTypes()

    fun color(): Color = when (this) {
        is LowWarning -> TangerineYellow
        is MediumWarning -> AtomicTangerine
        is HighWarning -> Mahogany
        else -> White
    }
}
