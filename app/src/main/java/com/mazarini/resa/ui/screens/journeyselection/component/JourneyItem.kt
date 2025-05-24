package com.mazarini.resa.ui.screens.journeyselection.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.Mdivider
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews

@Composable
fun JourneyItem(
    modifier: Modifier = Modifier,
    journey: Journey,
    onEvent: (JourneySelectionUiEvent) -> Unit = {},
    isSingleJourney: Boolean,
    onJourneyClicked: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .testTag("JourneyItem_${journey.id}")
            .clickable { onJourneyClicked(journey.id) },
    ) {
        if (journey.isOnlyWalk().not()) {
            JourneyItemHeader(
                modifier = Modifier.padding(horizontal = 24.dp),
                journey = journey,
            )
            JourneyTimeDetail(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 24.dp),
                journey = journey,
            )
            Mdivider(
                modifier = Modifier
                    .padding(top = 6.dp, start = 24.dp)
            )
        } else {
            WalkOnlyJourney(
                modifier = Modifier.padding(horizontal = 24.dp),
                leg = journey.legs.first(),
                onEvent = onEvent,
            )
            Mdivider(
                modifier = Modifier
                    .padding(top = 6.dp, start = 24.dp)
            )
            if (isSingleJourney) {
                JourneysEmptyState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                )
            }
        }
    }
}

private fun Journey.isOnlyWalk(): Boolean =
    legs.size == 1 && legs.first().transportMode == TransportMode.walk

@Composable
@Previews
fun TSearchItemPreview() {
    ResaTheme {
        JourneyItem(
            modifier = Modifier.background(Color.White),
            journey = FakeFactory.journey(),
            isSingleJourney = false,
            onJourneyClicked = {},
        )
    }
}