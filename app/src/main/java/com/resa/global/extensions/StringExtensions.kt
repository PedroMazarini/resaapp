package com.resa.global.extensions

import androidx.compose.ui.graphics.Color
import com.resa.ui.theme.colors.Blueberry

fun String.asColor(): Color {
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        Blueberry
    }
}
