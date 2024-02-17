package com.resa.ui.screens.locationsearch

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.resa.R
import com.resa.global.extensions.isNotNull
import com.resa.global.extensions.toggle
import com.resa.global.fake.FakeFactory
import com.resa.ui.commoncomponents.LocationItem
import com.resa.ui.commoncomponents.journeySearchFilters.FiltersTopBar
import com.resa.ui.commoncomponents.journeySearchFilters.JourneyFilter
import com.resa.ui.commoncomponents.journeySearchFilters.getFilterDetailText
import com.resa.ui.commoncomponents.loading.LinearLoading
import com.resa.ui.model.Location
import com.resa.ui.screens.locationsearch.components.searchFields.SearchFields
import com.resa.ui.screens.locationsearch.state.CurrentSearchType
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent
import com.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.*
import com.resa.ui.screens.locationsearch.state.LocationSearchUiState
import com.resa.ui.screens.locationsearch.state.MIN_LENGTH_FOR_SEARCH
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.fontSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class,
)
@Composable
fun LocationSearchScreen(
    uiState: LocationSearchUiState,
    onEvent: (LocationSearchUiEvent) -> Unit,
    navToJourneySelection: () -> Unit,
    upPress: () -> Unit = {},
) {

    val isSelectionComplete by uiState.isSelectionComplete.collectAsState()
    val filters by uiState.journeyFilters
    val savedLocations by uiState.savedLocations
    val recentLocations by uiState.recentLocations
    val searchResults = fetchSearchResults(uiState)
    val currentSearchText by fetchCurrentSearchText(uiState)
    val shouldShowResults = shouldShowResults(uiState = uiState, currentSearchText)

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
                onDateChanged = { onEvent(DateFilterChanged(it)) },
                onTimeChanged = { onEvent(TimeFilterChanged(it)) },
                onReferenceChanged = { onEvent(IsDepartFilterChanged(it)) },
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
                    .background(MTheme.colors.background)
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
                    SearchFields(
                        uiState = uiState,
                        onEvent = onEvent,
                    )
                }
                if (shouldShowResults) {
                    items(
                        count = searchResults.itemCount,
                    ) { index ->
                        searchResults[index]?.let { location ->
                            val savedLocations by uiState.savedLocations
                            val isSaved = savedLocations.any { it.id == location.id }
                            LocationItem(
                                location = location,
                                highlight = currentSearchText.trimEnd(),
                                isSaved = isSaved,
                                onLocationSelected = { locationSelected ->
                                    onEvent(LocationSelected(locationSelected))
                                },
                                saveLocation = { locationSelected ->
                                    onEvent(SaveLocation(locationSelected))
                                },
                                deleteLocation = { id ->
                                    onEvent(DeleteLocation(id))
                                },
                            )
                        }
                    }
                } else {
                    if (savedLocations.isNotEmpty()) {
                        item {
                            SuggestionHeader(text = stringResource(R.string.saved))
                        }

                        items(savedLocations) { location ->
                            Suggestion(
                                location = location,
                                isSaved = true,
                                onEvent = onEvent,
                            )
                        }
                    }

                    if (recentLocations.isNotEmpty()) {
                        item {
                            SuggestionHeader(text = stringResource(R.string.recent))
                        }

                        items(recentLocations) { location ->
                            Suggestion(
                                location = location,
                                isSaved = false,
                                onEvent = onEvent,
                            )
                        }
                    }
                }
            }
            if (searchResults.loadState.append is LoadState.Loading && shouldShowResults) {
                LinearLoading(modifier = Modifier.align(Alignment.BottomCenter))
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

    if (isSelectionComplete) {
        onEvent(NavigateToResults)
        navToJourneySelection()
    }
}

@Composable
fun Suggestion(
    location: Location,
    isSaved: Boolean,
    onEvent: (LocationSearchUiEvent) -> Unit,
) {
    LocationItem(
        location = location,
        isSaved = isSaved,
        onLocationSelected = { locationSelected ->
            onEvent(LocationSelected(locationSelected))
        },
        saveLocation = { locationSelected ->
            onEvent(SaveLocation(locationSelected))
        },
        deleteLocation = { id ->
            onEvent(DeleteLocation(id))
        },
    )
}

@Composable
fun SuggestionHeader(text: String) {
    Text(
        modifier = Modifier
            .padding(start = 24.dp)
            .padding(vertical = 16.dp),
        text = text,
        style = MTheme.type.secondaryText.fontSize(16.sp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
fun handleBackPress(sheetState: SheetState, upPress: () -> Unit, scope: CoroutineScope) {
    if (sheetState.isVisible) scope.launch { sheetState.hide() } else upPress()
}

@Composable
fun fetchSearchResults(uiState: LocationSearchUiState): LazyPagingItems<Location> {
    val currentSearchType by uiState.currentSearchType.collectAsState()
    val originSearchRes = uiState.originSearchRes.value.collectAsLazyPagingItems()
    val destSearchRes = uiState.destSearchRes.value.collectAsLazyPagingItems()

    return when (currentSearchType) {
        CurrentSearchType.ORIGIN -> originSearchRes
        CurrentSearchType.DESTINATION -> destSearchRes
    }
}

@Composable
fun fetchCurrentSearchText(uiState: LocationSearchUiState): State<String> {
    val currentSearchType by uiState.currentSearchType.collectAsState()
    val originSelected by uiState.originSelected.collectAsState()
    val destSelected by uiState.destSelected.collectAsState()
    val originSearch by uiState.originSearch.collectAsState()
    val destSearch by uiState.destSearch.collectAsState()

    return remember {
        derivedStateOf {
            when (currentSearchType) {
                CurrentSearchType.ORIGIN -> {
                    if (originSelected.isNotNull) "" else originSearch
                }

                CurrentSearchType.DESTINATION -> {
                    if (destSelected.isNotNull) "" else destSearch
                }
            }
        }
    }
}

@Composable
fun shouldShowResults(
    uiState: LocationSearchUiState,
    currentSearchText: String,
): Boolean {
    val currentSearchType by uiState.currentSearchType.collectAsState()
    val originSelected by uiState.originSelected.collectAsState()
    val destSelected by uiState.destSelected.collectAsState()

    return currentSearchText.length >= MIN_LENGTH_FOR_SEARCH
            &&
            when (currentSearchType) {
                CurrentSearchType.ORIGIN -> originSelected == null
                CurrentSearchType.DESTINATION -> destSelected == null
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
@Preview
fun LocationSearchPreview() {
    val fakeFavoriteList = FakeFactory.locationList()
    ResaTheme {
        LocationSearchScreen(
            uiState = LocationSearchUiState(
                originSearchRes = mutableStateOf(flowOf(PagingData.from(fakeFavoriteList))),
                originSearch = MutableStateFlow("abc"),
            ),
            onEvent = {},
            navToJourneySelection = {},
        )
    }
}
