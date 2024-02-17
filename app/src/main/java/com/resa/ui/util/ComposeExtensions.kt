package com.resa.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.resa.R
import com.resa.domain.model.journey.JourneyTimes
import com.resa.ui.theme.MTheme
import kotlinx.coroutines.delay

fun JourneyTimes.diffFromNow(): Long =
    when (this) {
        is JourneyTimes.Planned -> {
            time.time - System.currentTimeMillis()
        }
        is JourneyTimes.Changed -> {
            estimated.time - System.currentTimeMillis()
        }
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
