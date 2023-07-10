package com.resa.ui.screens.journeyselection.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resa.R
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.journey.JourneyTimes
import com.resa.global.extensions.DAY_IN_MILI
import com.resa.global.extensions.date_MMM_dd
import com.resa.global.extensions.formatMinutes
import com.resa.global.extensions.time_HH_mm
import com.resa.global.fake.FakeFactory
import com.resa.ui.commoncomponents.LiveIcon
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.strikeThrough
import java.util.Date

@Composable
fun JourneyTimeDetail(
    modifier: Modifier,
    journey: Journey,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row {
            Text(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.arrive_at),
                style = MTheme.type.secondaryLightText,
            )

            ArriveTime(
                modifier = Modifier.align(Alignment.CenterVertically),
                journey.arrivalTimes,
            )

            if (journey.arrivalTimes.isLiveTracking) {
                LiveIcon()
            } else {
                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(id = R.drawable.ic_calendar_todo),
                    contentDescription = null,
                    tint = MTheme.colors.textSecondary,
                )
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.duration),
                style = MTheme.type.secondaryLightText,
            )
            Text(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .align(Alignment.CenterVertically),
                text = formatMinutes(journey.durationInMinutes),
                style = MTheme.type.secondaryText,
            )
        }
        LazyRow(modifier = Modifier.padding(top = 16.dp)) {
            itemsIndexed(journey.legs) { index, item ->
                LegItem(
                    leg = item,
                    hasLeadingArrow = index != 0,
                )
            }
        }
    }
}

@Composable
fun ArriveTime(
    modifier: Modifier,
    arrivalTimes: JourneyTimes,
) {

    val arrivalTime = when (arrivalTimes) {
        is JourneyTimes.Planned -> arrivalTimes.time
        is JourneyTimes.Changed -> arrivalTimes.estimated
    }

    if (arrivalTimes is JourneyTimes.Changed) {
        Text(
            modifier = modifier.padding(end = 4.dp),
            text = arrivalTimes.planned.time_HH_mm(),
            style = MTheme.type.secondaryLightText.strikeThrough(),
        )
    }

    Text(
        modifier = modifier.padding(end = 4.dp),
        text = formatForArrivalTime(arrivalTime),
        style = MTheme.type.secondaryText,
    )
}

@Composable
fun formatForArrivalTime(date: Date): String {
    val diff = date.time - Date().time
    val trailing = if (diff > DAY_IN_MILI) date.date_MMM_dd() else ""
    return date.time_HH_mm() + trailing
}

@Composable
@Preview
fun JourneyTimeDetailPreview() {
    ResaTheme {
        JourneyTimeDetail(
            modifier = Modifier.background(Color.White),
            journey = FakeFactory.journey(),
        )
    }
}