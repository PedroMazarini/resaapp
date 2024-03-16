package com.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.resa.domain.model.journey.JourneyTimes
import com.resa.domain.model.journey.Leg
import com.resa.global.fake.FakeFactory
import com.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import java.util.Date

@Composable
fun WalkLegDetails(
    modifier: Modifier = Modifier,
    leg: Leg,
    onEvent: (JourneyDetailsUiEvent) -> Unit = {},
) {

    Column(modifier = modifier) {
        LegNameTimeDetails(name = leg.name, legTime = leg.getTime(), warnings = leg.warnings)
        LegsDivider()
        WalkItem(
            modifier = Modifier,
            leg = leg,
            onEvent = onEvent,
        )
        if (leg is Leg.ArrivalLink) {
            LegNameTimeDetails(
                name = leg.destinationName,
                legTime = leg.arriveTime,
            )
        }
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
@Preview
fun DepartureWalkLegDetailsPreview() {
    ResaTheme {
        WalkLegDetails(
            modifier = Modifier.background(MTheme.colors.background),
            leg = FakeFactory.departWalkLeg(),
        )
    }
}

@Composable
@Preview
fun DepartureWalkLegDetailsChangedPreview() {
    ResaTheme {
        WalkLegDetails(
            modifier = Modifier.background(MTheme.colors.background),
            leg = FakeFactory.departWalkLeg(
                departTime = JourneyTimes.Changed(
                    planned = Date(),
                    estimated = Date(),
                    isLiveTracking = false,
                ),
                warnings = FakeFactory.mediumWarnings(),
            ),
        )
    }
}