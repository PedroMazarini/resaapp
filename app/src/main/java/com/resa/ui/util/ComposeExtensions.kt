package com.resa.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resa.R
import com.resa.domain.model.journey.JourneyTimes
import com.resa.global.extensions.date_MMM_dd
import com.resa.global.extensions.isAfter1h
import com.resa.global.extensions.isAfter24h
import com.resa.global.extensions.minutesFrom
import com.resa.global.extensions.time_HH_mm
import com.resa.global.extensions.withinAMinute
import com.resa.ui.theme.MTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@Composable
fun Int.dpToPx(): Int =
    with(LocalDensity.current) { this@dpToPx.dp.toPx() }.roundToInt()

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
) : MutableLongState {
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
    val minutes = TimeUnit.MINUTES.toMinutes( travelTimeInMinutes - (hours * 60))
    return if (hours > 0) {
        stringResource(id = R.string.abrev_h_m, hours, minutes)
    } else {
        stringResource(id = R.string.abrev_m, minutes)
    }
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
