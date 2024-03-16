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
    warningLow: Color,
    warningMedium: Color,
    warningHigh: Color,
    warningLowBg: Color,
    warningMediumBg: Color,
    warningHighBg: Color,
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
    var lightText by mutableStateOf(lightText)
        private set
    var ultraLightDetail by mutableStateOf(ultraLightDetail)
        private set
    var separatorLight by mutableStateOf(separatorLight)
        private set
    var shimmer by mutableStateOf(shimmer)
        private set
    var warningLow by mutableStateOf(warningLow)
        private set
    var warningMedium by mutableStateOf(warningMedium)
        private set
    var warningHigh by mutableStateOf(warningHigh)
        private set
    var warningLowBg by mutableStateOf(warningLowBg)
        private set
    var warningMediumBg by mutableStateOf(warningMediumBg)
        private set
    var warningHighBg by mutableStateOf(warningHighBg)
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
        warningLow = other.warningLow
        warningMedium = other.warningMedium
        warningHigh = other.warningHigh
        warningLowBg = other.warningLowBg
        warningMediumBg = other.warningMediumBg
        warningHighBg = other.warningHighBg
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
        warningLow = warningLow,
        warningMedium = warningMedium,
        warningHigh = warningHigh,
        warningLowBg = warningLowBg,
        warningMediumBg = warningMediumBg,
        warningHighBg = warningHighBg,
    )
}
