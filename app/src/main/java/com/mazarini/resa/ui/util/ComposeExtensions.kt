package com.mazarini.resa.ui.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.global.extensions.date_MMM_dd
import com.mazarini.resa.global.extensions.isAfter1h
import com.mazarini.resa.global.extensions.isAfter24h
import com.mazarini.resa.global.extensions.minutesFrom
import com.mazarini.resa.global.extensions.time_HH_mm
import com.mazarini.resa.global.extensions.withinAMinute
import com.mazarini.resa.ui.theme.MTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


@Composable
fun Int.dpToPx(): Int =
    with(LocalDensity.current) { this@dpToPx.dp.toPx() }.roundToInt()

@Composable
fun bitmapDescriptorFromRes(@DrawableRes resId: Int): BitmapDescriptor {
    val bitmap =
        drawableToBitmap(AppCompatResources.getDrawable(LocalContext.current, resId)!!)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

private fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }

    val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
        // Single color bitmap will be created of 1x1 pixel
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
    }

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

@Composable
fun Float.pxToDp(): Dp =
    (this / (LocalContext.current.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).dp

fun JourneyTimes.diffFromNow(): Long =
    when (this) {
        is JourneyTimes.Planned -> {
            time.time - System.currentTimeMillis()
        }

        is JourneyTimes.Changed -> {
            estimated.time - System.currentTimeMillis()
        }
    }

fun JourneyTimes.formatTime(): String =
    when (this) {
        is JourneyTimes.Planned -> time.time_HH_mm()
        is JourneyTimes.Changed -> estimated.time_HH_mm()
    }

@Composable
fun departMilliAsText(departInMilli: Long): String {
    var diffMinutes = departInMilli / (60 * 1000) % 60
    var diffHours = departInMilli / (60 * 60 * 1000) % 24
    return if (diffHours > 0) {
        stringResource(id = R.string.abrev_h_m, diffHours, diffMinutes)
    } else if (diffMinutes == 0L) {
        stringResource(id = R.string.now).lowercase()
    } else if (diffMinutes < 0L) {
        stringResource(id = R.string.departed)
    } else {
        stringResource(id = R.string.abrev_m, diffMinutes)
    }
}

@Composable
fun departMilliToStyle(departInMilli: Long): TextStyle {
    var diffMinutes = departInMilli / (60 * 1000) % 60
    var diffHours = departInMilli / (60 * 60 * 1000) % 24

    return if (diffHours > 0 || diffMinutes > 0) {
        MTheme.type.secondaryText.fontSize(12.sp)
    } else if (diffMinutes == 0L) {
        MTheme.type.secondaryText.fontSize(12.sp).copy(color = MTheme.colors.primary)
    } else {
        MTheme.type.secondaryText.fontSize(12.sp).copy(color = MTheme.colors.alert)
    }
}

enum class TimeUpdateInterval(val millis: Long) {
    SECOND(1000),
    TEN_SECONDS(10000),
    MINUTE(60000),
    HOUR(3600000)
}

@Composable
fun getTimeUpdate(
    interval: TimeUpdateInterval,
): MutableLongState {
    val timeMillis = remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(key1 = timeMillis) {
        while (true) {
            delay(interval.millis)
            timeMillis.longValue = System.currentTimeMillis()
        }
    }
    return timeMillis
}

@Composable
fun getTravelTimeText(travelTimeInMinutes: Long): String {
    val hours = TimeUnit.MINUTES.toHours(travelTimeInMinutes)
    val minutes = TimeUnit.MINUTES.toMinutes(travelTimeInMinutes - (hours * 60))
    return if (hours > 0) {
        stringResource(id = R.string.abrev_h_m, hours, minutes)
    } else {
        stringResource(id = R.string.abrev_m, minutes)
    }
}

fun JourneyTimes.diffFrom(now: Long): Long {
    val departDate = when (this) {
        is JourneyTimes.Planned -> time
        is JourneyTimes.Changed -> estimated
    }
    return departDate.time - now
}

@Composable
fun JourneyTimes.getDepartText(now: Long): String {
    val departDate = when (this) {
        is JourneyTimes.Planned -> time
        is JourneyTimes.Changed -> estimated
    }

    return when {
        departDate.isAfter24h() -> {
            stringResource(
                id = R.string.depart_future,
                departDate.date_MMM_dd(),
                departDate.time_HH_mm(),
            )
        }

        departDate.isAfter1h() -> {
            stringResource(
                id = R.string.depart_today,
                departDate.time_HH_mm(),
            )
        }

        departDate.withinAMinute(now) -> {
            stringResource(id = R.string.depart_now)
        }

        this.hasDeparted() -> {
            stringResource(
                id = R.string.departed_past,
                departDate.time_HH_mm(),
            )
        }

        else -> {
            stringResource(
                id = R.string.depart_in_m,
                departDate.minutesFrom(now),
            )
        }
    }
}

@Composable
fun getFormattedString(
    @StringRes resId: Int,
    baseStyle: SpanStyle,
    patternStyle: SpanStyle,
): AnnotatedString {
    val rawString = stringResource(id = resId)

    // Regex patterns to identify marked sections
    val pattern = "\\*(.*?)\\*".toRegex()

    return buildAnnotatedString {
        var lastEnd = 0

        // Function to process matches and apply styles
        fun processMatches(pattern: Regex, baseStyle: SpanStyle, patternStyle: SpanStyle) {
            pattern.findAll(rawString).forEach { matchResult ->
                val start = matchResult.range.first
                val end = matchResult.range.last + 1

                if (start > lastEnd) {
                    withStyle(baseStyle) {
                        append(rawString.substring(lastEnd, start))
                    }
                }

                withStyle(patternStyle) {
                    append(matchResult.groupValues[1])
                }
                lastEnd = end
            }
        }

        // Apply formatting
        processMatches(
            pattern = pattern,
            baseStyle = baseStyle,
            patternStyle = patternStyle,
        )

        // Append remaining text
        if (lastEnd < rawString.length) {
            withStyle(baseStyle) {
                append(rawString.substring(lastEnd))
            }
        }
    }
}
