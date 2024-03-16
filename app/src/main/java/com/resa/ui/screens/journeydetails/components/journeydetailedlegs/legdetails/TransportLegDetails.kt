package com.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resa.domain.model.journey.ArrivalLeg
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.Leg
import com.resa.domain.model.journey.LegDetails
import com.resa.domain.model.journey.LegDetails.None.departName
import com.resa.domain.model.journey.LegStop
import com.resa.global.extensions.formatMinutes
import com.resa.global.fake.FakeFactory
import com.resa.ui.commoncomponents.LegBoxIcon
import com.resa.ui.screens.journeydetails.components.WarningBar
import com.resa.ui.screens.journeydetails.components.journeydetailedlegs.LegTimeText
import com.resa.ui.screens.journeyselection.component.shimmerEffect
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import java.util.Date

@Composable
fun TransportLegDetails(
    modifier: Modifier = Modifier,
    leg: Leg.Transport,
) {

    if (leg.details is LegDetails.Details) {
        Column(modifier = modifier) {
            val rowBottomPadding = if (leg.details.legStops.isNotEmpty()) 0.dp else 16.dp
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp)
            ) {
                Column(modifier = Modifier
                    .padding(end = 4.dp)
                    .weight(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = rowBottomPadding)
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .align(Alignment.CenterVertically),
                            text = leg.details.departName,
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
            if (leg.arrivalLeg is ArrivalLeg.Details) {
                LegNameTimeDetails(
                    name = leg.arrivalLeg.name,
                    legTime = leg.arrivalLeg.arrivalTime,
                )
            }
        }
    } else {
        LoadingDetails()
    }
}

@Composable
private fun LegsDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = MTheme.colors.separatorLight,
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
                details = LegDetails.Details(
                    index = 1,
                    pathWay = listOf(),
                    legStops = listOf(
                        LegStop(
                            id = "1",
                            name = "Stop 1",
                            time = "19:00",
                        ),
                    ),
                    platForm = null,
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
                details = LegDetails.None,
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
                details = LegDetails.Details(
                    index = 1,
                    pathWay = listOf(),
                    legStops = listOf(
                        LegStop(
                            id = "1",
                            name = "Stop 1",
                            time = "19:00",
                        ),
                    ),
                    platForm = null,
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