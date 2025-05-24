package com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mazarini.resa.domain.model.journey.JourneyTimes
import com.mazarini.resa.domain.model.journey.Leg
import com.mazarini.resa.global.extensions.orFalse
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import java.util.Date

@Composable
fun WalkLegDetails(
    modifier: Modifier = Modifier,
    leg: Leg,
    onEvent: (JourneyDetailsUiEvent) -> Unit = {},
) {

    Column(modifier = modifier) {
        LegNameTimeDetails(
            name = leg.name,
            legTime = leg.departTime,
            warnings = leg.warnings,
            platform = leg.details?.platForm?.name ?: "",
        )
        LegsDivider()
        WalkItem(
            modifier = Modifier,
            leg = leg,
            onEvent = onEvent,
        )
        if (leg.details?.isLastLeg.orFalse && leg.details?.destination != null) {
            LegNameTimeDetails(
                name = leg.details.destination.name,
                legTime = leg.details.destination.arrivalTime,
            )
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