package com.resa.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.resa.R
import com.resa.ui.theme.colors.ResaColors

private val Rubik = FontFamily(
    Font(R.font.rubik_regular, FontWeight.Normal),
    Font(R.font.rubik_black, FontWeight.Black),
    Font(R.font.rubik_bold, FontWeight.Bold),
    Font(R.font.rubik_semibold, FontWeight.SemiBold),
    Font(R.font.rubik_light, FontWeight.Light),
    Font(R.font.rubik_medium, FontWeight.Medium),
)

@Stable
class ResaTypography(colors: ResaColors) {

    val fadedTextS = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colors.textSecondary,
    )

    val highlightTitle = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = colors.textPrimary,
    )

    val fadedText = SpanStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colors.textSecondary,
    )

    val highlightText = SpanStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = colors.textPrimary,
    )
    val highlightTextS = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = colors.textPrimary,
    )
    val highlightTitleS = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = colors.textPrimary,
    )

    val secondaryText = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        color = colors.textSecondary,
    )

    val secondaryLightText = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = colors.lightText,
    )

    val tertiaryLightText = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        color = colors.lightText,
    )

    val textField = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = colors.textPrimary,
    )
    val textFieldPlaceHolder = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = colors.textSecondary,
    )

    val hoursStyle = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = colors.primary,
    )
}

val DebugTypography = Typography(
    displayLarge = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    displayMedium = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    displaySmall = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    headlineLarge = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    headlineMedium = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    headlineSmall = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    titleLarge = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    titleMedium = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    titleSmall = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    bodyLarge = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    bodyMedium = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    bodySmall = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    labelLarge = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    labelMedium = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
    labelSmall = TextStyle(letterSpacing = 0.sp, color = Color.Red, fontSize = 9.sp),
)

@Composable
fun ProvideTypography(
    resaColors: ResaColors,
    content: @Composable () -> Unit,
) {
    val resaTypography = ResaTypography(resaColors)
    CompositionLocalProvider(LocalResaTypography provides resaTypography, content = content)
}

val LocalResaTypography = staticCompositionLocalOf<ResaTypography> {
    error("No TextStyles provided")
}