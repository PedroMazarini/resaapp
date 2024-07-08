package com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mazarini.resa.domain.model.journey.Leg
import com.mazarini.resa.domain.model.journey.LegType
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun LegItem(
    modifier: Modifier = Modifier,
    leg: Leg,
    onEvent: (JourneyDetailsUiEvent) -> Unit = {}
) {
    when (leg.legType) {
        LegType.TRANSPORT -> TransportLegDetails(modifier = modifier, leg = leg)
        LegType.DEPART_LINK,
        LegType.CONNECTION_LINK,
        LegType.ARRIVAL_LINK,
        LegType.DIRECT_LINK -> WalkLegDetails(
            leg = leg,
            onEvent = onEvent,
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