package com.resa.ui.screens.journeyselection.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
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
import com.resa.domain.model.journey.Journey
import com.resa.domain.model.journey.JourneyTimes
import com.resa.global.extensions.date_MMM_dd
import com.resa.global.extensions.isAfter1h
import com.resa.global.extensions.isAfter24h
import com.resa.global.extensions.minutesFromNow
import com.resa.global.extensions.time_HH_mm
import com.resa.global.fake.FakeFactory
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.asAlert
import com.resa.ui.util.fontSize
import com.resa.ui.util.strikeThrough

@Composable
fun JourneyItemHeader(
    modifier: Modifier,
    journey: Journey,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Column {
                Text(
                    modifier = Modifier,
                    text = getDepartText(journey),
                    style = departTextStyling(
                        journey.isDeparted ||
                                journey.departure.hasPassed()
                    ),
                )
                if (journey.departure.time is JourneyTimes.Changed) {
                    Text(
                        modifier = Modifier.align(End),
                        text = journey.departure.time.planned.time_HH_mm(),
                        style = MTheme.type.secondaryText.strikeThrough(),
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 16.dp),
                verticalAlignment = CenterVertically,
            ) {
                if (journey.hasAlert) {
                    AlertIcon()
                }
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = journey.departure.departStopName,
                    style = MTheme.type.secondaryText,
                )
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = stringResource(
                        id = R.string.platform_name,
                        journey.departure.departPlatform
                    ),
                    style = MTheme.type.secondaryLightText,
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
        Box(
            modifier = Modifier
                .padding(start = 12.dp)
                .clip(RoundedCornerShape(4.dp))
                .size(48.dp)
                .background(MTheme.colors.primary),
            contentAlignment = Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.go),
                textAlign = TextAlign.Center,
                style = MTheme.type.highlightTitleS.copy(Color.White),
            )
        }
    }
}

@Composable
fun getDepartText(journey: Journey): String {
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

        journey.isDeparted -> {
            stringResource(
                id = R.string.departed_past,
                departDate.time_HH_mm(),
            )
        }

        else -> {
            stringResource(
                id = R.string.depart_in_m,
                departDate.minutesFromNow(),
            )
        }
    }
}

@Composable
fun departTextStyling(hasDeparted: Boolean): TextStyle {
    val style = MTheme.type.highlightTextS.fontSize(20.sp)
    if (hasDeparted) return style.asAlert()

    return style
}

@Composable
fun AlertIcon() {
    Icon(
        modifier = Modifier
            .padding(end = 12.dp)
            .size(14.dp),
        painter = painterResource(id = R.drawable.ic_alert),
        tint = MTheme.colors.alert,
        contentDescription = null,
    )
}

@Composable
@Preview
fun TSearchDepartPreview() {
    ResaTheme {
        JourneyItemHeader(
            modifier = Modifier.background(color = Color.White),
            journey = FakeFactory.journey(),
        )
    }
}
