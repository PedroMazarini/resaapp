package com.mazarini.resa.global.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mazarini.resa.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

const val DAY_IN_MILI = 86400000
const val FIFTY_FOUR_MIN_IN_MILI = 3540000
//const val RFC_3339 = "yyyy-MM-dd'T'HH:mm:ssXXX"
const val RFC_3339 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX"

fun Date.plusSec(sec: Int): Date {
    val now = Calendar.getInstance()
    now.add(Calendar.SECOND, sec)
    return now.time
}

infix fun Date.addDays(days: Int): Date {
    val now = Calendar.getInstance()
    now.add(Calendar.DAY_OF_YEAR, days)
    return now.time
}

infix fun Date.minusMinutes(minutes: Int): Date {
    val now = Calendar.getInstance()
    now.add(Calendar.MINUTE, minutes * -1)
    return now.time
}

infix fun Date.withinAMinute(now: Long) = this.time - now in (-60000..0)


infix fun Date.minusDays(days: Int): Date {
    return this addDays -days
}

fun Date.hasExpired(toleranceInMin: Int): Boolean {
    val now = Calendar.getInstance()
    now.add(Calendar.MINUTE, toleranceInMin)
    return this.before(now.time)
}

fun Date.rfc3339() = SimpleDateFormat(RFC_3339).format(this)

fun String.parseRfc3339() = SimpleDateFormat(RFC_3339).parse(this)

fun Date.time_HH_mm() = SimpleDateFormat("HH:mm").format(this)

fun String.toDate_HH_mm() = SimpleDateFormat("HH:mm").parse(this)

fun Date.time_HH_mm_spaced() = SimpleDateFormat("HH : mm").format(this)

fun Date.time_HH_mm_ss() = SimpleDateFormat("HH:mm:ss").format(this)

fun Date.date_MMM_dd() = SimpleDateFormat("MMM/dd").format(this)

fun Date.date_MMM_dd_spaced() = SimpleDateFormat("MMM / dd").format(this)

@Composable
fun formatMinutes(minutes: Int): String {
    val sdf = SimpleDateFormat("mm")

    val date = Date(minutes * 60L * 1000L)
    val formattedMinutes = sdf.format(date)
    val h = stringResource(id = R.string.hour_abbreviation)
    val m = stringResource(id = R.string.min)

    return if (minutes >= 60) {
        val hours = minutes / 60
        val formattedHours = hours.toString().padStart(2, '0')
        "$formattedHours$h $formattedMinutes$m"
    } else {
        formattedMinutes+m
    }
}


fun Date.setDateOnly(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    with(date) {
        calendar.set(Calendar.YEAR, year())
        calendar.set(Calendar.MONTH, month())
        calendar.set(Calendar.DAY_OF_MONTH, day())
    }
    return calendar.time
}

fun Date.isToday(): Boolean {
    val today = Date()
    return year() == today.year()
            && month() == today.month()
            && day() == today.day()
}

fun Date.isAfter24h(): Boolean = this.time > Date().time + DAY_IN_MILI

fun Date.isAfter1h(): Boolean = this.time > Date().time + FIFTY_FOUR_MIN_IN_MILI

fun Date.hasPassed(): Boolean = this.time < Date().time

fun Date.minutesFromNow(): Int = kotlin.math.ceil((this.time - Date().time) / 60_000.0).toInt()

fun Date.minutesFrom(millis: Long): Int = kotlin.math.ceil((this.time - millis) / 60_000.0).toInt()

fun Date.setHourMinute(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, date.getHour())
    calendar.set(Calendar.MINUTE, date.getMinute())
    return calendar.time
}

fun Date.year(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.YEAR)
}

fun Date.month(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MONTH)
}

fun Date.day(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun Date.getMinute(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MINUTE)
}

fun Date.getHour(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun Date.addMinutes(minutes: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MINUTE, minutes)
    return calendar.time
}