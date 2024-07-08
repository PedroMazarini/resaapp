package com.mazarini.resa.domain.model.journey

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.mazarini.resa.domain.serializers.ColorSerializer
import com.mazarini.resa.ui.theme.LocalResaIsDarkTheme
import com.mazarini.resa.ui.theme.colors.FrenchBlue
import kotlinx.serialization.Serializable

@Serializable
data class LegColors(
    @Serializable(with = ColorSerializer::class)
    val foreground: Color,
    @Serializable(with = ColorSerializer::class)
    val background: Color,
    @Serializable(with = ColorSerializer::class)
    val border: Color,
) {

    @Composable
    fun primaryColor() = if (hasBorder()) foreground else background
    @Composable
    fun secondaryColor() = if (hasBorder()) background else foreground

    @Composable
    fun hasBorder(): Boolean {
        return if (background.luminance() > 0.15f) {
            !LocalResaIsDarkTheme.current
        } else {
            LocalResaIsDarkTheme.current
        }
    }
}

fun LegColors?.orDefault(): LegColors =
    this ?:
    LegColors(
        foreground = Color.White,
        background = FrenchBlue,
        border = FrenchBlue,
    )
