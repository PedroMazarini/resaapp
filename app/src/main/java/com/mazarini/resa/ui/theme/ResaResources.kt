package com.mazarini.resa.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.mazarini.resa.R

val LocalResaResources = staticCompositionLocalOf<ResaResources> {
    error("No ResaResources provided")
}

@Composable
fun ProvideResaResources(
    resaResources: ResaResources,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalResaResources provides resaResources, content = content)
}
data class ResaResources(
    @DrawableRes val mapBg: Int,
)

internal val lightResources = ResaResources(
    mapBg = R.drawable.map_bg_light,
)

internal val darkResources = ResaResources(
    mapBg = R.drawable.map_bg_dark,
)