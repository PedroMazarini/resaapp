package com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.domain.model.journey.Leg
import com.mazarini.resa.domain.model.journey.LegDetails
import com.mazarini.resa.domain.model.journey.LegStop
import com.mazarini.resa.domain.model.journey.Platform
import com.mazarini.resa.domain.model.journey.departName
import com.mazarini.resa.global.extensions.formatMinutes
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.LegBoxIcon
import com.mazarini.resa.ui.screens.journeydetails.components.WarningBar
import com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.LegTimeText
import com.mazarini.resa.ui.screens.journeyselection.component.shimmerEffect
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.strikeThrough
import java.util.Date

@Composable
fun TransportLegDetails(
    modifier: Modifier = Modifier,
    leg: Leg,
) {

    leg.details?.let { details ->
        Column(modifier = modifier) {
            val rowBottomPadding = if (details.legStops.isNotEmpty()) 0.dp else 16.dp
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp)
            ) {
                Column(modifier = Modifier
                    .padding(end = 4.dp)
                    .weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .align(Alignment.CenterVertically),
                            text = leg.directionName,
                            style = MTheme.type.secondaryText.copy(fontSize = 14.sp),
                            overflow = TextOverflow.Visible,
                            maxLines = 4,
                        )
                        LegBoxIcon(
                            modifier = Modifier
                                .align(Alignment.Top)
                                .padding(start = 12.dp),
                            legName = leg.name,
                            legColors = leg.colors,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = rowBottomPadding)
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Top),
                            text = stringResource(id = R.string.at_time),
                            style = MTheme.type.secondaryLightText.copy(fontSize = 14.sp),
                            overflow = TextOverflow.Visible,
                            maxLines = 4,
                        )
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp),
                            text = leg.details.departName,
                            style = MTheme.type.secondaryText.copy(fontSize = 14.sp),
                            overflow = TextOverflow.Visible,
                            maxLines = 4,
                        )
                    }
                    leg.details.platForm?.let {
                        Text(
                            modifier = Modifier
                                .padding(top = 4.dp),
                            text = getPlatform(it),
                            style = MTheme.type.secondaryLightText.copy(fontSize = 14.sp),
                            overflow = TextOverflow.Visible,
                            maxLines = 4,
                        )
                    }
                    if (leg.warnings.isNotEmpty()) {
                        WarningBar(
                            modifier = Modifier.padding(end = 8.dp, top = 8.dp),
                            warnings = leg.warnings,
                        )
                    }
                    if (leg.details.legStops.isNotEmpty()) {
                        LegStops(
                            modifier = Modifier
                                .padding(vertical = 16.dp),
                            legTotalTime = formatMinutes(leg.durationInMinutes),
                            legStops = leg.details.legStops,
                        )
                    }
                }

                LegTimeText(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.Top),
                    legTime = leg.departTime,
                )
            }
            LegsDivider()
            if (details.isLastLeg && details.destination != null) {
                LegNameTimeDetails(
                    name = details.destination.name,
                    platform = details.destination.platform?.name.orEmpty(),
                    legTime = details.destination.arrivalTime,
                )
            }
        }
    } ?: run {
        LoadingDetails()
    }
}

@Composable
fun getPlatform(platform: Platform): AnnotatedString {
    return when (platform) {
        is Platform.Changed -> {
            buildAnnotatedString {
                withStyle(MTheme.type.secondaryLightText.toSpanStyle()) {
                    append(stringResource(id = R.string.platform_name, ""))
                }
                append(" ")
                withStyle(MTheme.type.secondaryLightText.strikeThrough().toSpanStyle()) {
                    append(platform.oldName)
                }
                append(" ")
                withStyle(
                    MTheme.type.secondaryLightText.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                ).toSpanStyle()) {
                    append(platform.name)
                }
            }
        }
        is Platform.Planned -> {
            buildAnnotatedString {
                withStyle(MTheme.type.secondaryLightText.toSpanStyle()) {
                    append(stringResource(id = R.string.platform_name, ""))
                }
                append(" ")
                withStyle(
                    MTheme.type.secondaryLightText.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                ).toSpanStyle()) {
                    append(platform.name)
                }
            }
        }
    }
}

@Composable
private fun LegsDivider() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),
        color = MTheme.colors.graph.minimal,
        thickness = 1.dp,
    )
}

@Composable
fun LoadingDetails() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(0.5f)
                    .height(16.dp)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(start = 16.dp)
                    .size(16.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(end = 24.dp)
                    .height(16.dp)
                    .width(32.dp)
                    .shimmerEffect(),
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .padding(start = 16.dp)
                    .height(16.dp)
                    .shimmerEffect()
            )
        }
    }
}

@Composable
@Preview
fun TransportLegDetailsPreview() {
    ResaTheme {
        TransportLegDetails(
            modifier = Modifier.background(MTheme.colors.background),
            leg = FakeFactory.leg(
                index = 1,
                details = LegDetails(
                    index = 1,
                    legStops = listOf(
                        LegStop(
                            id = "1",
                            name = "Stop 1 long name very long name stop 1 very long name two lines",
                            time = "19:00",
                            coordinate = null,
                            isLegStart = true,
                            isPartOfLeg = true,
                            isLegEnd = false,
                        ),
                    ),
                    platForm = null,
                    isLastLeg = false,
                )
            ),
        )
    }
}

@Composable
@Preview
fun TransportLegDetailsLoadingPreview() {
    ResaTheme {
        TransportLegDetails(
            modifier = Modifier.background(MTheme.colors.background),
            leg = FakeFactory.leg(
                index = 1,
                details = null,
                departTime = JourneyTimes.Changed(
                    estimated = Date(),
                    planned = Date(),
                    isLiveTracking = true,
                )
            ),
        )
    }
}

@Composable
@Preview
fun TransportLegDetailsPlatformTimePreview() {
    ResaTheme {
        TransportLegDetails(
            modifier = Modifier.background(MTheme.colors.background),
            leg = FakeFactory.leg(
                index = 1,
                details = LegDetails(
                    index = 1,
                    legStops = listOf(
                        LegStop(
                            id = "1",
                            name = "Stop 1",
                            time = "19:00",
                            coordinate = null,
                            isLegStart = true,
                            isPartOfLeg = true,
                            isLegEnd = false,
                        ),
                    ),
                    platForm = Platform.Planned("A"),
                    isLastLeg = false,
                ),
                departTime = JourneyTimes.Changed(
                    estimated = Date(),
                    planned = Date(),
                    isLiveTracking = true,
                )
            ),
        )
    }
}

@Composable
@Preview
fun TransportLegDetailsChangedTimePreview() {
    ResaTheme {
        TransportLegDetails(
            modifier = Modifier.background(MTheme.colors.background),
            leg = FakeFactory.leg(
                index = 1,
                details = LegDetails(
                    index = 1,
                    legStops = listOf(
                        LegStop(
                            id = "1",
                            name = "Stop 1",
                            time = "19:00",
                            coordinate = null,
                            isLegStart = true,
                            isPartOfLeg = true,
                            isLegEnd = false,
                        ),
                    ),
                    platForm = Platform.Changed("A", "B"),
                    isLastLeg = false,
                ),
                departTime = JourneyTimes.Changed(
                    estimated = Date(),
                    planned = Date(),
                    isLiveTracking = true,
                )
            ),
        )
    }
}