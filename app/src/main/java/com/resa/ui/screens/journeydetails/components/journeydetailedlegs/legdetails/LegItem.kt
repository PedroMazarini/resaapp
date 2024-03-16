package com.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.resa.domain.model.journey.Leg
import com.resa.global.fake.FakeFactory
import com.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.resa.ui.theme.ResaTheme

@Composable
fun LegItem(
    modifier: Modifier = Modifier,
    leg: Leg,
    onEvent: (JourneyDetailsUiEvent) -> Unit = {}
) {
    when (leg) {
        is Leg.Transport -> TransportLegDetails(modifier = modifier, leg = leg)
        is Leg.DepartureLink -> WalkLegDetails(
            leg = leg,
            onEvent = onEvent,
        )
        is Leg.AccessLink -> WalkLegDetails(
            modifier = modifier,
            leg = leg,
            onEvent = onEvent,
        )
        is Leg.ArrivalLink -> WalkLegDetails(
            modifier = modifier,
            leg = leg, onEvent = onEvent,
        )
    }
}

@Composable
@Preview
fun JourneyDetailedLegItemPreview() {
    ResaTheme {
        LegItem(
            leg = FakeFactory.leg()
        )
    }
}