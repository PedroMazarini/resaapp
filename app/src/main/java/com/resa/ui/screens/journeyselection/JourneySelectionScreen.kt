package com.resa.ui.screens.journeyselection

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.resa.R
import com.resa.global.extensions.toggle
import com.resa.global.fake.FakeFactory
import com.resa.ui.application.PayloadType
import com.resa.ui.commoncomponents.journeySearchFilters.FiltersTopBar
import com.resa.ui.commoncomponents.journeySearchFilters.JourneyFilter
import com.resa.ui.commoncomponents.journeySearchFilters.getFilterDetailText
import com.resa.ui.commoncomponents.loading.LinearLoading
import com.resa.ui.screens.journeyselection.component.JourneyItem
import com.resa.ui.screens.journeyselection.component.JourneyRouteSelected
import com.resa.ui.screens.journeyselection.component.TripSearchLoading
import com.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent
import com.resa.ui.screens.journeyselection.state.JourneySelectionUiState
import com.resa.ui.screens.locationsearch.handleBackPress
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun JourneySelectionScreen(
    uiState: JourneySelectionUiState,
    onEvent: (JourneySelectionUiEvent) -> Unit,
    navigateToLocationSearch: (PayloadType) -> Unit,
    upPress: () -> Unit,
) {

    val journeysResult by uiState.journeysResult
    val journeysPaging = journeysResult.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        if (journeysPaging.loadState.prepend is LoadState.Loading) {
            TripSearchLoading()
        } else {
            JourneySelectionList(
                uiState = uiState,
                onEvent = onEvent,
                navigateToLocationSearch = navigateToLocationSearch,
                upPress = upPress,
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun JourneySelectionList(
    uiState: JourneySelectionUiState,
    onEvent: (JourneySelectionUiEvent) -> Unit,
    navigateToLocationSearch: (PayloadType) -> Unit,
    upPress: () -> Unit,
) {
    val journeysResult by uiState.journeysResult
    val journeysPaging = journeysResult.collectAsLazyPagingItems()

    val filters by uiState.journeyFilters

    /** States */
    val sheetState = getBottomSheetState()
    val scaffoldState = getBottomSheetScaffoldState(sheetState = sheetState)
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = Modifier,
        sheetShape = RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp),
        scaffoldState = scaffoldState,
        sheetShadowElevation = 8.dp,
        sheetPeekHeight = 0.dp,
        sheetDragHandle = null,
        sheetContent = {
            JourneyFilter(
                filters = filters,
                onDateChanged = { onEvent(JourneySelectionUiEvent.DateFilterChanged(it)) },
                onTimeChanged = { onEvent(JourneySelectionUiEvent.TimeFilterChanged(it)) },
                onReferenceChanged = { onEvent(JourneySelectionUiEvent.IsDepartFilterChanged(it)) },
            ) {
                scope.launch { sheetState.hide() }
            }
        }
    ) {
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
                        isFilterSheetOpen = sheetState.isVisible,
                        filterDetail = getFilterDetailText(filters),
                        onIconClicked = {
                            handleBackPress(
                                sheetState = sheetState,
                                scope = scope,
                                upPress = upPress,
                            )
                        },
                    ) {
                        scope.launch {
                            sheetState.toggle()
                        }
                    }
                }
                item {
                    JourneyRouteSelected(
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .padding(top = 48.dp),
                        uiState = uiState,
                        navigateToLocationSearch = navigateToLocationSearch,
                    )
                }
                items(
                    key = {
                        journeysPaging[it]?.id ?: ""
                    },
                    count = journeysPaging.itemCount,
                ) { index ->
                    journeysPaging[index]?.let { journey ->
                        JourneyItem(
                            journey = journey,
                        ) {

                        }
                    }
                }
            }

            if (sheetState.isVisible) {
                LocalSoftwareKeyboardController.current?.hide()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 56.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            scope.launch { sheetState.hide() }
                        }
                        .background(
                            color = MTheme.colors.textSecondary
                        ),
                ) {}
            }
            BackHandler(sheetState.isVisible) {
                scope.launch { sheetState.hide() }
            }
        }
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
                journeysResult = mutableStateOf(
                    flowOf(PagingData.from(FakeFactory.journeyList())),
                )),
            navigateToLocationSearch = {},
            onEvent = {},
            upPress = {},
        )
    }
}