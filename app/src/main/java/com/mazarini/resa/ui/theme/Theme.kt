package com.mazarini.resa.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.mazarini.resa.ui.theme.colors.Blue500
import com.mazarini.resa.ui.theme.colors.DarkColorPalette
import com.mazarini.resa.ui.theme.colors.LightColorPalette
import com.mazarini.resa.ui.theme.colors.LocalResaColors
import com.mazarini.resa.ui.theme.colors.ProvideResaColors
import com.mazarini.resa.ui.theme.colors.ResaColors

@Composable
fun ResaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    val resaResource = if (darkTheme) darkResources else lightResources
    val mapsStyling = if (darkTheme) GoogleMapsStyling.DARK else GoogleMapsStyling.LIGHT

    ProvideResaColors(colors = colors) {
        ProvideTypography(resaColors = colors) {
            ProvideMapsStyling(googleMapsStyling = mapsStyling) {
                ProvideResaResources(resaResources = resaResource) {
                    ProvideSelectedTheme(darkTheme = darkTheme) {
                        MaterialTheme(
                            colorScheme = debugColorScheme,
                            typography = DebugTypography,
                            content = {
                                Surface(color = MTheme.colors.background) {content()}
                            },
                        )
                    }
                }
            }
        }
    }
}

object MTheme {
    val colors: ResaColors
        @Composable
        get() = LocalResaColors.current
    val type: ResaTypography
        @Composable
        get() = LocalResaTypography.current
    val res: ResaResources
        @Composable
        get() = LocalResaResources.current
    val mapsStyling: GoogleMapsStyling
        @Composable
        get() = LocalResaMapsStyling.current
}

@Composable
fun ProvideSelectedTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalResaIsDarkTheme provides darkTheme, content = content)
}

val LocalResaIsDarkTheme = staticCompositionLocalOf<Boolean> {
    error("No ResaTheme provided")
}

private val debugColorScheme = darkColorScheme(
    primary = Blue500,
    onPrimary = Blue500,
    primaryContainer = Blue500,
    onPrimaryContainer = Blue500,
    inversePrimary = Blue500,
    secondary = Blue500,
    onSecondary = Blue500,
    secondaryContainer = Blue500,
    onSecondaryContainer = Blue500,
    tertiary = Blue500,
    onTertiary = Blue500,
    tertiaryContainer = Blue500,
    onTertiaryContainer = Blue500,
    background = Blue500,
    onBackground = Blue500,
    surface = Blue500,
    onSurface = Blue500,
    surfaceVariant = Blue500,
    onSurfaceVariant = Blue500,
    surfaceTint = Blue500,
    inverseSurface = Blue500,
    inverseOnSurface = Blue500,
    error = Blue500,
    onError = Blue500,
    errorContainer = Blue500,
    onErrorContainer = Blue500,
    outline = Blue500,
    outlineVariant = Blue500,
    scrim = Blue500,
)