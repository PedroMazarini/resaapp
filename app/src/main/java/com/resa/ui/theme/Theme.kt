package com.resa.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.resa.ui.theme.colors.DarkColorPalette
import com.resa.ui.theme.colors.LightColorPalette
import com.resa.ui.theme.colors.LocalResaColors
import com.resa.ui.theme.colors.ProvideResaColors
import com.resa.ui.theme.colors.Purple80
import com.resa.ui.theme.colors.ResaColors

@Composable
fun ResaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
//    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = LightColorPalette //if (darkTheme) DarkColorPalette else LightColorPalette

//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        else -> debugColorScheme
//    }
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
//    }

    ProvideResaColors(colors = colors) {
        ProvideTypography(resaColors = colors) {
            MaterialTheme(
                colorScheme = debugColorScheme,
                typography = DebugTypography,
                content = content
            )
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
}

private val debugColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple80,
    primaryContainer = Purple80,
    onPrimaryContainer = Purple80,
    inversePrimary = Purple80,
    secondary = Purple80,
    onSecondary = Purple80,
    secondaryContainer = Purple80,
    onSecondaryContainer = Purple80,
    tertiary = Purple80,
    onTertiary = Purple80,
    tertiaryContainer = Purple80,
    onTertiaryContainer = Purple80,
    background = Purple80,
    onBackground = Purple80,
    surface = Purple80,
    onSurface = Purple80,
    surfaceVariant = Purple80,
    onSurfaceVariant = Purple80,
    surfaceTint = Purple80,
    inverseSurface = Purple80,
    inverseOnSurface = Purple80,
    error = Purple80,
    onError = Purple80,
    errorContainer = Purple80,
    onErrorContainer = Purple80,
    outline = Purple80,
    outlineVariant = Purple80,
    scrim = Purple80,
)