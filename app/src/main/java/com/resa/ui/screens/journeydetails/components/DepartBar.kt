package com.resa.ui.screens.journeydetails.components

import androidx.compose.foundation.background
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
import com.resa.R
import com.resa.domain.model.journey.JourneyTimes
import com.resa.global.extensions.addMinutes
import com.resa.ui.commoncomponents.Animation
import com.resa.ui.commoncomponents.LottieAnim
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.TimeUpdateInterval
import com.resa.ui.util.asPrimary
import com.resa.ui.util.getDepartText
import com.resa.ui.util.getTimeUpdate
import com.resa.ui.util.strikeThrough
import java.util.Date

@Composable
fun DepartBar(
    modifier: Modifier = Modifier,
    depart: JourneyTimes,
    travelTime: String,
) {
    val now = getTimeUpdate(interval = TimeUpdateInterval.TEN_SECONDS)

    Row(
        modifier = modifier
            .background(color = MTheme.colors.background)
            .padding(end = 24.dp, start = 22.dp)
            .padding(top = 20.dp, bottom = 16.dp),
    ) {
        DepartText(depart, now.longValue)

        Text(
            modifier = Modifier
                .padding(end = 12.dp)
                .align(Alignment.CenterVertically),
            text = stringResource(id = R.string.travel_time),
            style = MTheme.type.secondaryLightText,
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = travelTime,
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
            travelTime = "15min",
        )
    }
}

@Composable
@Preview
fun TripDetailsDepartPastPreview() {
    ResaTheme(darkTheme = false) {
        DepartBar(
            depart = JourneyTimes.Planned(Date().addMinutes(-10), isLiveTracking = false),
            travelTime = "15min",
        )
    }
}

@Composable
@Preview
fun TripDetailsDepartFuturePreview() {
    ResaTheme(darkTheme = false) {
        DepartBar(
            depart = JourneyTimes.Planned(Date().addMinutes(95), isLiveTracking = false),
            travelTime = "15min",
        )
    }
}
