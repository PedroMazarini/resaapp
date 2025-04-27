package com.mazarini.resa.ui.screens.journeydetails

import android.Manifest
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mazarini.resa.domain.model.TransportMode
import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.global.extensions.safeLet
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.permissions.Common
import com.mazarini.resa.ui.screens.journeydetails.components.AddToHomeButton
import com.mazarini.resa.ui.screens.journeydetails.components.LegsSummaryBar
import com.mazarini.resa.ui.screens.journeydetails.components.DepartBar
import com.mazarini.resa.ui.screens.journeydetails.components.DetailsTopBar
import com.mazarini.resa.ui.screens.journeydetails.components.JourneyDisclaimers
import com.mazarini.resa.ui.screens.journeydetails.components.LoadingJourneyDetails
import com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.LegsLayout
import com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.legdetails.LegItem
import com.mazarini.resa.ui.screens.journeydetails.components.journeydetailedlegs.sidebar.LegsSideBar
import com.mazarini.resa.ui.screens.journeydetails.components.map.DetailsMap
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiState
import com.mazarini.resa.ui.screens.journeydetails.components.map.MapCard
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.getTravelTimeText
import com.mazarini.resa.ui.util.mapsconfiguration.openWalkDirectionsOnMaps
import com.mazarini.resa.ui.util.pxToDp
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun JourneyDetailsScreen(
    uiState: JourneyDetailsUiState,
    onEvent: (JourneyDetailsUiEvent) -> Unit = {},
) {
    val selectedJourney by uiState.selectedJourney.collectAsState()
    val hasCheckedForLocationAccess by uiState.hasCheckedForLocationAccess
//    val isCurrentJourneyAddedHome by uiState.isCurrentJourneyAddedHome
    val shouldShowMap by uiState.shouldShowMap
    var shadowEffect by remember { mutableStateOf(0.dp) }
    val offSetPx = remember { mutableFloatStateOf(0f) }
//    val showAddButton by remember { derivedStateOf {
//        offSetPx.floatValue == 0f && !isCurrentJourneyAddedHome
//    }}
    val context = LocalContext.current

    selectedJourney?.let { journey ->
        if (shouldShowMap) {
            DetailsMap(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                onEvent = onEvent,
            )
        } else {
            Box {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MTheme.colors.background)
                ) {
                    DetailsTopBar(
                        modifier = Modifier
                            .shadow(shadowEffect)
                            .zIndex(1f)
                            .background(MTheme.colors.background)
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        onEvent = onEvent,
                        uiState = uiState,
                    )
                    Column(
                        modifier = Modifier
                            .offset(y = offSetPx.floatValue.pxToDp())
                    ) {
                        LegsSummaryBar(
                            modifier = Modifier
                                .background(MTheme.colors.background)
                                .padding(top = 0.dp),
                            legs = journey.legs,
                        )
                        MapCard(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                            uiState = uiState,
                            onEvent = onEvent,
                        )
                        DepartBar(
                            modifier = Modifier
                                .background(MTheme.colors.background),
                            depart = journey.departure.time,
                            journeyTime = getTravelTimeText(journey.durationInMinutes.toLong()),
                            transportTime = getJourneyTransportTime(journey),
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
                            LegItem(
                                leg = leg,
                                onEvent = { interceptEvent(it, onEvent, context) },
                            )
                        },
                        disclaimerItem = {
                            JourneyDisclaimers(
                                modifier = Modifier,
                            )
                        },
                        isScrolling = { isScrolling ->
                            shadowEffect = if (isScrolling) 8.dp else 0.dp
                        },
                        scrollTo = { position ->
                            offSetPx.floatValue = -position
                        },
                    )
                }
//                AddToHomeButton(
//                    modifier = Modifier
//                        .align(Alignment.BottomEnd)
//                        .padding(end = 24.dp, bottom = 24.dp),
//                    onEvent = onEvent,
//                    visible = showAddButton,
//                )
            }
        }
    } ?: run {
        LoadingJourneyDetails()
    }

    if (!hasCheckedForLocationAccess) {
        CheckLocationAccess(onEvent)
    }

    LaunchedEffect(Unit) {
        onEvent(JourneyDetailsUiEvent.SaveJourneyToHome)
    }
}

@Composable
private fun CheckLocationAccess(onEvent: (JourneyDetailsUiEvent) -> Unit) {

    val hasPermission = Common.checkIfPermissionGranted(
        LocalContext.current,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    onEvent(JourneyDetailsUiEvent.LocationAccessResult(hasPermission))
}

fun interceptEvent(
    it: JourneyDetailsUiEvent,
    onEvent: (JourneyDetailsUiEvent) -> Unit,
    context: Context
) {
    if (it is JourneyDetailsUiEvent.OnLegMapClicked) {
        safeLet(it.direction.from, it.direction.to) { from, to ->
            openWalkDirectionsOnMaps(from, to, context)
        }
    } else {
        onEvent(it)
    }
}

@Composable
fun getJourneyTransportTime(journey: Journey): String? {
    if (journey.legs.first().transportMode == TransportMode.walk ||
        journey.legs.last().transportMode == TransportMode.walk) {
        return getTravelTimeText(journey.transportDurationInMinutes.toLong())
    }
    return null
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
