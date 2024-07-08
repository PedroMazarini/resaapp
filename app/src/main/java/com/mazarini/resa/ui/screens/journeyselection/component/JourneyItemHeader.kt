package com.mazarini.resa.ui.screens.journeyselection.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.domain.model.journey.LegColors
import com.mazarini.resa.domain.model.journey.WarningTypes
import com.mazarini.resa.global.extensions.hasPassed
import com.mazarini.resa.global.extensions.isAfter1h
import com.mazarini.resa.global.extensions.time_HH_mm
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.Animation
import com.mazarini.resa.ui.commoncomponents.LottieAnim
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.TimeUpdateInterval
import com.mazarini.resa.ui.util.asAlert
import com.mazarini.resa.ui.util.asPrimary
import com.mazarini.resa.ui.util.color
import com.mazarini.resa.ui.util.fontSize
import com.mazarini.resa.ui.util.getDepartText
import com.mazarini.resa.ui.util.getTimeUpdate
import com.mazarini.resa.ui.util.strikeThrough

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
                                    .color(MTheme.colors.graph.disabled)
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
                        Icon(
                            modifier = Modifier
                                .padding(start = 4.dp, top = 4.dp)
                                .align(CenterVertically)
                                .size(12.dp),
                            painter = painterResource(id = R.drawable.ic_calendar_todo),
                            contentDescription = null,
                            tint = MTheme.colors.graph.normal,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp),
                verticalAlignment = CenterVertically,
            ) {
                Row (
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    HeaderLegBoxIcon(
                        modifier = Modifier,
                        legName = journey.departure.lineName,
                        legColors = journey.departure.colors,
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = journey.departure.directionName,
                        style = MTheme.type.secondaryText,
                    )
                }
                if (journey.warning != WarningTypes.NoWarning) {
                    WarningIcon(journey.warning.color())
                }
                if (journey.hasAccessibility) {
                    Icon(
                        modifier = Modifier.height(14.dp),
                        painter = painterResource(id = R.drawable.ic_accessibility),
                        contentDescription = null,
                        tint = MTheme.colors.graph.normal,
                    )
                }
            }
            Row(
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.at_location),
                    style = MTheme.type.secondaryLightText,
                )
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = journey.departure.departStopName,
                    style = MTheme.type.secondaryText,
                )
            }
        }
        if (journey.departure.departPlatform.isNotEmpty()) {
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
}

@Composable
private fun HeaderLegBoxIcon(
    modifier: Modifier,
    legName: String,
    legColors: LegColors,
) {
    val borderSize = if (legColors.hasBorder()) 1.dp else 0.dp
    Card(
        modifier = modifier.height(IntrinsicSize.Min),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = legColors.background,
        ),
        border = BorderStroke(borderSize, legColors.foreground),
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp),
                text = legName,
                textAlign = TextAlign.Center,
                style = MTheme.type.tertiaryLightText
                    .copy(color = legColors.foreground),
                maxLines = 1,
                fontSize = 10.sp,
            )
        }
    }
}

@Composable
fun getDepartText(journey: Journey, now: Long): String {
    val departDate = when (val departTime = journey.departure.time) {
        is JourneyTimes.Planned -> departTime.time
        is JourneyTimes.Changed -> departTime.estimated
    }
    return journey.departure.time.getDepartText(now)
}

@Composable
fun departTextStyling(journey: Journey, now: Long): TextStyle {
    val style = MTheme.type.highlightTextS.fontSize(20.sp)
    if (journey.departure.sameMinute(now)) return style.asPrimary()
    if (journey.isDeparted || journey.departure.isBefore(now)) return style.asAlert()

    return style
}

@Composable
fun WarningIcon(color: Color) {
    Icon(
        modifier = Modifier
            .padding(end = 12.dp)
            .size(12.dp),
        painter = painterResource(id = R.drawable.ic_alert),
        tint = color,
        contentDescription = null,
    )
}

@Composable
@Preview
fun TSearchDepartDarkPreview() {
    ResaTheme(darkTheme = true) {
        JourneyItemHeader(
            modifier = Modifier,
            journey = FakeFactory.journey(isLive = false),
        )
    }
}

@Composable
@Preview
fun TSearchDepartLiveDarkPreview() {
    ResaTheme(darkTheme = true) {
        JourneyItemHeader(
            modifier = Modifier,
            journey = FakeFactory.journey(),
        )
    }
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