package com.mazarini.resa.ui.theme.colors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun ProvideResaColors(
    colors: ResaColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalResaColors provides colorPalette, content = content)
}

val LocalResaColors = staticCompositionLocalOf<ResaColors> {
    error("No ResaColorPalette provided")
}