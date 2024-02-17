package com.resa.ui.screens.journeyselection.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resa.R
import com.resa.domain.model.TransportMode
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.WarningTypes
import com.resa.global.extensions.date_MMM_dd
import com.resa.global.extensions.hasPassed
import com.resa.global.extensions.isAfter1h
import com.resa.global.extensions.isAfter24h
import com.resa.global.extensions.minutesFrom
import com.resa.global.extensions.time_HH_mm
import com.resa.global.fake.FakeFactory
import com.resa.ui.commoncomponents.Animation
import com.resa.ui.commoncomponents.LottieAnim
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.TimeUpdateInterval
import com.resa.ui.util.asAlert
import com.resa.ui.util.color
import com.resa.ui.util.fontSize
import com.resa.ui.util.getTimeUpdate
import com.resa.ui.util.strikeThrough

@Composable
fun JourneyItemHeader(
    modifier: Modifier,
    journey: Journey,
) {
    val now = getTimeUpdate(interval = TimeUpdateInterval.TEN_SECONDS)

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.align(CenterVertically),
                        text = getDepartText(journey, now.longValue),
                        style = departTextStyling(journey, now.longValue),
                    )
                    if (journey.departure.time is JourneyTimes.Changed) {
                        Column(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .padding(bottom = 2.dp)
                                .align(Bottom)
                        ) {
                            Text(
                                modifier = Modifier,
                                text = journey.departure.time.planned.time_HH_mm(),
                                style = MTheme.type.secondaryText
                                    .color(MTheme.colors.lightText)
                                    .strikeThrough(),
                            )
                            Text(
                                modifier = Modifier,
                                text = journey.departure.time.estimated.time_HH_mm(),
                                style = MTheme.type.secondaryText,
                            )
                        }
                    } else if ((journey.departure.time is JourneyTimes.Planned) &&
                        (journey.departure.time.time.isAfter1h().not()) &&
                        (journey.departure.time.time.hasPassed().not())
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .padding(bottom = 2.dp)
                                .align(Bottom)
                        ) {
                            Text(
                                modifier = Modifier,
                                text = journey.departure.time.time.time_HH_mm(),
                                style = MTheme.type.secondaryText,
                            )
                        }
                    }

                    if (journey.arrivalTimes.isLiveTracking) {
                        LottieAnim(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(20.dp)
                                .align(CenterVertically),
                            animation = Animation.LIVE,
                        )
                    } else {
                        if (journey.isOnlyWalk().not()) {
                            Icon(
                                modifier = Modifier
                                    .padding(start = 4.dp, top = 4.dp)
                                    .align(CenterVertically)
                                    .size(12.dp),
                                painter = painterResource(id = R.drawable.ic_calendar_todo),
                                contentDescription = null,
                                tint = MTheme.colors.textSecondary,
                            )
                        }
                    }
                }
            }

            if (journey.isOnlyWalk().not()) {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    verticalAlignment = CenterVertically,
                ) {
                    if (journey.warning != WarningTypes.NoWarning) {
                        WarningIcon(journey.warning.color())
                    }
                    Text(
                        modifier = Modifier.padding(end = 12.dp),
                        text = journey.departure.departStopName,
                        style = MTheme.type.secondaryText,
                    )
                    if (journey.hasAccessibility) {
                        Icon(
                            modifier = Modifier.height(14.dp),
                            painter = painterResource(id = R.drawable.ic_accessibility),
                            contentDescription = null,
                            tint = MTheme.colors.lightText,
                        )
                    }
                }
            }
        }
        Column {
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .size(48.dp)
                    .background(MTheme.colors.primary),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = journey.departure.departPlatform,
                    textAlign = TextAlign.Center,
                    style = MTheme.type.highlightTitleS.copy(color = Color.White, fontSize = 20.sp),
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.platform),
                    textAlign = TextAlign.Center,
                    style = MTheme.type.secondaryLightText.copy(
                        color = Color.White,
                        fontSize = 8.sp
                    ),
                )
            }
        }
    }
}

@Composable
fun getDepartText(journey: Journey, now: Long): String {
    val departDate = when (val departTime = journey.departure.time) {
        is JourneyTimes.Planned -> departTime.time
        is JourneyTimes.Changed -> departTime.estimated
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

        journey.isOnlyWalk() -> {
            stringResource(
                id = R.string.depart_now,
                departDate.minutesFrom(now),
            )
        }

        journey.isDeparted || journey.departure.isBefore(now) -> {
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

private fun Journey.isOnlyWalk(): Boolean =
    legs.size == 1 && legs.first().transportMode == TransportMode.walk

@Composable
fun departTextStyling(journey: Journey, now: Long): TextStyle {
    val style = MTheme.type.highlightTextS.fontSize(20.sp)
    if (journey.isOnlyWalk()) return style
    if (journey.isDeparted || journey.departure.isBefore(now)) return style.asAlert()

    return style
}

@Composable
fun WarningIcon(color: Color) {
    Icon(
        modifier = Modifier
            .padding(end = 12.dp)
            .padding(bottom = 2.dp)
            .size(12.dp),
        painter = painterResource(id = R.drawable.ic_alert),
        tint = color,
        contentDescription = null,
    )
}

@Composable
@Preview
fun TSearchDepartPreview() {
    ResaTheme {
        JourneyItemHeader(
            modifier = Modifier.background(color = Color.White),
            journey = FakeFactory.journey(isLive = false),
        )
    }
}

@Composable
@Preview
fun TSearchDepartLivePreview() {
    ResaTheme {
        JourneyItemHeader(
            modifier = Modifier.background(color = Color.White),
            journey = FakeFactory.journey(),
        )
    }
}