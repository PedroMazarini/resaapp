package com.resa.ui.screens.journeydetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.resa.global.fake.FakeFactory
import com.resa.ui.screens.journeydetails.components.LegsSummaryBar
import com.resa.ui.screens.journeydetails.components.DepartBar
import com.resa.ui.screens.journeydetails.components.JourneyPathBar
import com.resa.ui.screens.journeydetails.components.journeydetailedlegs.LegsLayout
import com.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails.LegItem
import com.resa.ui.screens.journeydetails.components.journeydetailedlegs.sidebar.LegsSideBar
import com.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.resa.ui.screens.journeydetails.state.JourneyDetailsUiState
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.getTravelTimeText
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun JourneyDetailsScreen(
    uiState: JourneyDetailsUiState,
    onEvent: (JourneyDetailsUiEvent) -> Unit = {},
) {
    val selectedJourney by uiState.selectedJourney.collectAsState()
    var shadowEffect by remember { mutableStateOf(0.dp) }

    selectedJourney?.let { journey ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(MTheme.colors.background)
        ) {
            Column(
                modifier = Modifier
                    .zIndex(1f)
                    .shadow(shadowEffect)
            ) {
                JourneyPathBar(
                    modifier = Modifier
                        .background(MTheme.colors.background)
                        .padding(horizontal = 24.dp),
                    uiState = uiState,
                    onEvent = onEvent,
                )
                LegsSummaryBar(
                    modifier = Modifier
                        .background(MTheme.colors.background)
                        .padding(top = 16.dp),
                    legs = journey.legs,
                )
                DepartBar(
                    modifier = Modifier
                        .background(MTheme.colors.background),
                    depart = journey.departure.time,
                    travelTime = getTravelTimeText(journey.durationInMinutes.toLong()),
                )
            }
            LegsLayout(
                modifier = Modifier,
                legs = journey.legs,
                sideBar = { leg ->
                    LegsSideBar(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        leg = leg,
                    )
                },
                legItem = { leg ->
                    LegItem(leg = leg)
                },
                isScrolling = { isScrolling ->
                    shadowEffect = if (isScrolling) 8.dp else 0.dp
                }
            )
        }
    } ?: run {
        Text(text = "No journey selected")
    }
}

@Composable
@Preview
fun TripDetailsScreenPreview() {
    ResaTheme {
        JourneyDetailsScreen(
            uiState = JourneyDetailsUiState(
                selectedJourney = MutableStateFlow(
                    FakeFactory.journey()
                ),
            ),
            onEvent = {},
        )
    }
}
