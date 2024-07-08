package com.mazarini.resa.global.model

import com.mazarini.resa.R

enum class ThemeSettings {
    LIGHT,
    DARK,
    SYSTEM;

    fun stringRes(): Int {
        return when (this) {
            LIGHT -> R.string.light_theme
            DARK -> R.string.dark_theme
            SYSTEM -> R.string.phone_default
        }
    }
}