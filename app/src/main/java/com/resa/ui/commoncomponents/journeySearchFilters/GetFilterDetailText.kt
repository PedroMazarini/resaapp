package com.resa.ui.commoncomponents.journeySearchFilters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.resa.R
import com.resa.global.extensions.addDays
import com.resa.global.extensions.date_MMM_dd
import com.resa.global.extensions.isToday
import com.resa.global.extensions.time_HH_mm
import com.resa.ui.screens.locationsearch.model.JourneyFilters
import com.resa.ui.theme.ResaTheme
import java.util.Date

@Composable
fun getFilterDetailText(filters: JourneyFilters): String {
    val isDepartureFilters = filters.isDepartureFilters
    val isToday = filters.date.isToday()

    return when {
        isDepartureFilters && isToday -> {
            stringResource(
                id = R.string.depart_today,
                filters.date.time_HH_mm(),
            )
        }

        isDepartureFilters -> {
            stringResource(
                id = R.string.depart_future,
                filters.date.date_MMM_dd(),
                filters.date.time_HH_mm(),
            )
        }

        isToday -> {
            stringResource(
                id = R.string.arrive_today,
                filters.date.time_HH_mm(),
            )
        }

        else -> {
            stringResource(
                id = R.string.arrive_future,
                filters.date.date_MMM_dd(),
                filters.date.time_HH_mm(),
            )
        }
    }
}

@Composable
@Preview
fun GetFilterDetailTextPreview() {
    ResaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            val text = getFilterDetailText(
                filters = JourneyFilters()
            )
            Text(text = text)
        }
    }
}

@Composable
@Preview
fun GetFilterDetailTextDepartFuturePreview() {
    ResaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            val text = getFilterDetailText(
                filters = JourneyFilters(date = Date() addDays 1),
            )
            Text(text = text)
        }
    }
}

@Composable
@Preview
fun GetFilterDetailTextArriveTodayPreview() {
    ResaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            val text = getFilterDetailText(
                filters = JourneyFilters(
                    isDepartureFilters = false,
                )
            )
            Text(text = text)
        }
    }
}

@Composable
@Preview
fun GetFilterDetailTextArriveFuturePreview() {
    ResaTheme {
        Column(modifier = Modifier.background(Color.White)) {
            val text = getFilterDetailText(
                filters = JourneyFilters(
                    isDepartureFilters = false,
                    date = Date() addDays 1,
                )
            )
            Text(text = text)
        }
    }
}
