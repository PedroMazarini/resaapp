package com.resa.ui.screens.journeyselection.component

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resa.R
import com.resa.domain.model.TransportMode
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.journey.JourneyTimes
import com.resa.global.extensions.DAY_IN_MILI
import com.resa.global.extensions.date_MMM_dd
import com.resa.global.extensions.formatMinutes
import com.resa.global.extensions.time_HH_mm
import com.resa.global.fake.FakeFactory
import com.resa.ui.commoncomponents.OccupancyLevelItem
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
        if (journey.isOnlyWalk().not()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .align(Alignment.CenterVertically),
                    text = stringResource(id = R.string.arrive_at),
                    style = MTheme.type.secondaryLightText,
                )

                ArriveTime(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    journey,
                )

                Spacer(modifier = Modifier.weight(1f))

                OccupancyLevelItem(
                    modifier = Modifier
                        .padding(start = 12.dp),
                    occupancyLevel = journey.occupancyLevel,
                )
            }
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

private fun Journey.isOnlyWalk(): Boolean =
    legs.size == 1 && legs.first().transportMode == TransportMode.walk

@Composable
fun ArriveTime(
    modifier: Modifier,
    journey: Journey,
) {

    val arrivalTime = when (journey.arrivalTimes) {
        is JourneyTimes.Planned -> journey.arrivalTimes.time
        is JourneyTimes.Changed -> journey.arrivalTimes.estimated
    }

    if (journey.arrivalTimes is JourneyTimes.Changed) {
        Text(
            modifier = modifier.padding(end = 4.dp),
            text = journey.arrivalTimes.planned.time_HH_mm(),
            style = MTheme.type.secondaryLightText.strikeThrough(),
        )
    }

    Text(
        modifier = modifier.padding(end = 4.dp),
        text = formatForArrivalTime(arrivalTime),
        style = MTheme.type.secondaryText,
    )
    Text(
        modifier = Modifier
            .padding(end = 12.dp),
        text = stringResource(id = R.string.in_time, formatMinutes(journey.durationInMinutes)),
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