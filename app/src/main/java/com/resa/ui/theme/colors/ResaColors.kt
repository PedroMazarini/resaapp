package com.resa.ui.theme.colors

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Stable
class ResaColors(
    primary: Color,
    secondary: Color,
    background: Color,
    textPrimary: Color,
    textSecondary: Color,
    textTertiary: Color,
    alert: Color,
    icon: Color,
    iconBackground: Color,
    btnBackground: Color,
    btnSecondaryBkg: Color,
    lightDetail: Color,
    lightText: Color,
    ultraLightDetail: Color,
    separatorLight: Color,
    shimmer: Color,
) {
    var primary by mutableStateOf(primary)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var background by mutableStateOf(background)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var textTertiary by mutableStateOf(textTertiary)
        private set
    var alert by mutableStateOf(alert)
        private set
    var icon by mutableStateOf(icon)
        private set
    var iconBackground by mutableStateOf(iconBackground)
        private set
    var btnBackground by mutableStateOf(btnBackground)
        private set
    var btnSecondaryBkg by mutableStateOf(btnSecondaryBkg)
        private set
    var lightDetail by mutableStateOf(lightDetail)
        private set
    var lightText by mutableStateOf(lightDetail)
        private set
    var ultraLightDetail by mutableStateOf(ultraLightDetail)
        private set
    var separatorLight by mutableStateOf(separatorLight)
        private set
    var shimmer by mutableStateOf(shimmer)
        private set

    fun update(other: ResaColors) {
        primary = other.primary
        secondary = other.secondary
        background = other.background
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        textTertiary = other.textTertiary
        alert = other.alert
        icon = other.icon
        iconBackground = other.iconBackground
        btnBackground = other.btnBackground
        btnSecondaryBkg = other.btnSecondaryBkg
        lightDetail = other.lightDetail
        lightText = other.lightText
        ultraLightDetail = other.ultraLightDetail
        separatorLight = other.separatorLight
        shimmer = other.shimmer
    }

    fun copy(): ResaColors = ResaColors(
        primary = primary,
        secondary = secondary,
        background = background,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        textTertiary = textTertiary,
        alert = alert,
        icon = icon,
        iconBackground = iconBackground,
        btnBackground = btnBackground,
        btnSecondaryBkg = btnSecondaryBkg,
        lightDetail = lightDetail,
        lightText = lightText,
        ultraLightDetail = ultraLightDetail,
        separatorLight = separatorLight,
        shimmer = shimmer,
    )
}
