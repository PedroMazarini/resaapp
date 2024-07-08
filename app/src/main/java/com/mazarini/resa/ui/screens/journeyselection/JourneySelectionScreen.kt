package com.mazarini.resa.ui.screens.journeyselection

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.safeLet
import com.mazarini.resa.global.extensions.toggle
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.FiltersTopBar
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.JourneyFilter
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.getFilterDetailText
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiEvent
import com.mazarini.resa.ui.navigation.Route
import com.mazarini.resa.ui.screens.journeyselection.component.JourneyItem
import com.mazarini.resa.ui.screens.journeyselection.component.JourneyRouteSelected
import com.mazarini.resa.ui.screens.journeyselection.component.JourneysEmptyState
import com.mazarini.resa.ui.screens.journeyselection.component.JourneysTab
import com.mazarini.resa.ui.screens.journeyselection.component.ShimmerJourneyItem
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiState
import com.mazarini.resa.ui.screens.locationsearch.handleBackPress
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews
import com.mazarini.resa.ui.util.mapsconfiguration.openWalkDirectionsOnMaps
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun JourneySelectionScreen(
    uiState: JourneySelectionUiState,
    onEvent: (JourneySelectionUiEvent) -> Unit,
    navigateTo: (route: Route) -> Unit = {},
    upPress: () -> Unit,
) {


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        JourneySelectionList(
            uiState = uiState,
            onEvent = onEvent,
            navigateTo = navigateTo,
            upPress = upPress,
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
)
@Composable
fun JourneySelectionList(
    uiState: JourneySelectionUiState,
    onEvent: (JourneySelectionUiEvent) -> Unit,
    navigateTo: (route: Route) -> Unit = {},
    upPress: () -> Unit,
) {
    val upcomingJourneysResult by uiState.upcomingJourneys
    val upcomingJourneysPaging = upcomingJourneysResult.collectAsLazyPagingItems()

    val passedJourneysResult by uiState.passedJourneys
    val passedJourneysPaging = passedJourneysResult.collectAsLazyPagingItems()

    val isUpcoming = remember { mutableStateOf(true) }
    val showFilters = remember { mutableStateOf(false) }

    /** States */
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MTheme.colors.background),
        ) {
            stickyHeader {
                FiltersTopBar(
                    iconId = R.drawable.ic_back,
                    filterDetail = getFilterDetailText(uiState.filterUiState.filters.value),
                    onIconClicked = {
                        handleBackPress(
                            sheetState = modalSheetState,
                            scope = scope,
                            upPress = upPress,
                        )
                    },
                ) {
                    onEvent(JourneySelectionUiEvent.UpdateJourneySearch)
                    showFilters.toggle()
                }
            }
            item {
                JourneyRouteSelected(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .padding(top = 24.dp),
                    uiState = uiState,
                    onEvent = onEvent,
                )
            }
            item {
                if (upcomingJourneysPaging.loadState.refresh == LoadState.Loading
                    || upcomingJourneysPaging.itemCount > 0
                ) {
                    JourneysTab(
                        modifier = Modifier.padding(top = 16.dp),
                        isUpcoming = isUpcoming.value,
                    ) {
                        isUpcoming.value = it
                    }
                }
            }
            if (isUpcoming.value) {
                items(
                    count = upcomingJourneysPaging.itemCount,
                ) { index ->
                    upcomingJourneysPaging[index]?.let { journey ->
                        JourneyItem(
                            journey = journey,
                            isSingleJourney = upcomingJourneysPaging.itemCount == 1,
                            onEvent = { interceptEvent(it, onEvent, context) },
                        ) {
                            onEvent(JourneySelectionUiEvent.JourneySelected(journey))
                            navigateTo(Route.JourneyDetails)
                        }
                    }
                }
            } else {
                items(
                    count = passedJourneysPaging.itemCount,
                ) { index ->
                    passedJourneysPaging[index]?.let { journey ->
                        JourneyItem(
                            journey = journey,
                            isSingleJourney = false,
                            onJourneyClicked = {
                                onEvent(JourneySelectionUiEvent.JourneySelected(journey))
                                navigateTo(Route.JourneyDetails)
                            }
                        )
                    }
                }
            }

            if (upcomingJourneysPaging.loadState.refresh == LoadState.Loading) {
                items(8) {
                    ShimmerJourneyItem(
                        modifier = Modifier
                            .clickable(enabled = false) {}
                            .padding(horizontal = 24.dp, vertical = 24.dp)
                    )
                }
            }
        }

        if (upcomingJourneysPaging.loadState.refresh != LoadState.Loading &&
            upcomingJourneysPaging.itemCount == 0
        ) {
            JourneysEmptyState(modifier = Modifier.fillMaxSize())
        }

        if (modalSheetState.isVisible) {
            LocalSoftwareKeyboardController.current?.hide()
        }
        BackHandler(modalSheetState.isVisible) {
            onEvent(JourneySelectionUiEvent.UpdateJourneySearch)
            scope.launch { modalSheetState.hide() }
        }
    }

    if (showFilters.value) {
        ModalBottomSheet(
            modifier = Modifier,
            shape = RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp),
            sheetState = modalSheetState,
            scrimColor = MTheme.colors.surfaceBlur,
            dragHandle = null,
            tonalElevation = 8.dp,
            onDismissRequest = { showFilters.value = false }
        ) {
            JourneyFilter(
                uiState = uiState.filterUiState,
                onEvent = { interceptFilterEvents(it, onEvent) },
            ) {
                onEvent(JourneySelectionUiEvent.UpdateJourneySearch)
                scope.launch { modalSheetState.hide() }
                showFilters.value = false
            }
        }
    }
}

private fun interceptEvent(
    it: JourneySelectionUiEvent,
    onEvent: (JourneySelectionUiEvent) -> Unit,
    context: Context
) {
    if (it is JourneySelectionUiEvent.OnLegMapClicked) {
        safeLet(it.direction.from, it.direction.to) { from, to ->
            openWalkDirectionsOnMaps(from, to, context)
        }
    } else {
        onEvent(it)
    }
}

private fun interceptFilterEvents(
    it: JourneyFilterUiEvent,
    onEvent: (JourneySelectionUiEvent) -> Unit,
) {
    when (it) {
        is JourneyFilterUiEvent.OnDateChanged -> onEvent(
            JourneySelectionUiEvent.DateFilterChanged(
                it.date
            )
        )

        is JourneyFilterUiEvent.OnTimeChanged -> onEvent(
            JourneySelectionUiEvent.TimeFilterChanged(
                it.date
            )
        )

        is JourneyFilterUiEvent.OnReferenceChanged -> onEvent(
            JourneySelectionUiEvent.IsDepartFilterChanged(
                it.isDepart
            )
        )

        is JourneyFilterUiEvent.OnModesChanged -> {
            onEvent(JourneySelectionUiEvent.TransportModesChanged)
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun getBottomSheetState() =
    SheetState(
        initialValue = SheetValue.Hidden,
        skipPartiallyExpanded = true
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun getBottomSheetScaffoldState(sheetState: SheetState) =
    rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState,
    )


@SuppressLint("UnrememberedMutableState")
@Composable
@Previews
fun JourneySelectionScreenPreview() {
    ResaTheme {
        JourneySelectionScreen(
            uiState = JourneySelectionUiState(
                upcomingJourneys = mutableStateOf(
                    flowOf(PagingData.from(FakeFactory.journeyList())),
                )
            ),
            onEvent = {},
            navigateTo = {},
            upPress = {},
        )
    }
}