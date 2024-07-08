package com.mazarini.resa.ui.screens.journeydetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.global.extensions.addMinutes
import com.mazarini.resa.ui.commoncomponents.Animation
import com.mazarini.resa.ui.commoncomponents.LottieAnim
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.TimeUpdateInterval
import com.mazarini.resa.ui.util.asPrimary
import com.mazarini.resa.ui.util.getDepartText
import com.mazarini.resa.ui.util.getTimeUpdate
import com.mazarini.resa.ui.util.strikeThrough
import java.util.Date

@Composable
fun DepartBar(
    modifier: Modifier = Modifier,
    depart: JourneyTimes,
    journeyTime: String,
    transportTime: String?,
) {
    val now = getTimeUpdate(interval = TimeUpdateInterval.TEN_SECONDS)

    Row(
        modifier = modifier
            .background(color = MTheme.colors.background)
            .padding(end = 24.dp, start = 22.dp)
            .padding(top = 20.dp, bottom = 16.dp),
    ) {
        DepartText(depart, now.longValue)

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            transportTime?.let {
                JourneyTimeText(
                    journeyTimeTitle = stringResource(id = R.string.transport_time),
                    journeyTime = transportTime,
                )
            }
            JourneyTimeText(
                journeyTimeTitle = stringResource(id = R.string.journey_time),
                journeyTime = journeyTime,
            )
        }
    }
}

@Composable
fun ColumnScope.JourneyTimeText(
    journeyTimeTitle: String,
    journeyTime: String,
) {
    Row(
        modifier = Modifier.align(Alignment.End)
    ) {
        Text(
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically),
            text = journeyTimeTitle,
            style = MTheme.type.secondaryLightText,
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = journeyTime,
            style = MTheme.type.secondaryText,
        )
    }
}

@Composable
private fun RowScope.DepartText(depart: JourneyTimes, now: Long) {
    val style =
        when {
            depart.isWithinAMinute() -> MTheme.type.highlightTextS.asPrimary()
            depart.hasDeparted() -> MTheme.type.secondaryLightText.strikeThrough()
            else -> MTheme.type.highlightTextS
        }
    Row (
        modifier = Modifier.weight(1f)
    ) {
        Text(
            modifier = Modifier.alignByBaseline(),
            text = depart.getDepartText(now = now),
            style = style.copy(fontSize = 16.sp),
        )
        if (depart.isLiveTracking) {
            LottieAnim(
                modifier = Modifier
                    .size(12.dp)
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically),
                animation = Animation.LIVE,
            )
        } else {
            Icon(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(12.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_calendar_todo),
                contentDescription = null,
                tint = MTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
@Preview
fun TripDetailsDepartPreview() {
    ResaTheme(darkTheme = false) {
        DepartBar(
            depart = JourneyTimes.Planned(Date(), isLiveTracking = false),
            journeyTime = "15min",
            transportTime = "5min",
        )
    }
}

@Composable
@Preview
fun TripDetailsDepartPastPreview() {
    ResaTheme(darkTheme = false) {
        DepartBar(
            depart = JourneyTimes.Planned(Date().addMinutes(-10), isLiveTracking = false),
            journeyTime = "15min",
            transportTime = "5min",
        )
    }
}

@Composable
@Preview
fun TripDetailsDepartFuturePreview() {
    ResaTheme(darkTheme = false) {
        DepartBar(
            depart = JourneyTimes.Planned(Date().addMinutes(95), isLiveTracking = false),
            journeyTime = "15min",
            transportTime = null,
        )
    }
}
