package com.mazarini.resa.ui.screens.locationsearch

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.isNotNull
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.FiltersTopBar
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.JourneyFilter
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.getFilterDetailText
import com.mazarini.resa.ui.commoncomponents.loading.LinearLoading
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.navigation.Route
import com.mazarini.resa.ui.screens.locationsearch.components.LocationItem
import com.mazarini.resa.ui.screens.locationsearch.components.searchFields.SearchFields
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentSearchType
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DeleteLocation
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.FilterEvent
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.LocationSelected
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.NavigateToResults
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.SaveLocation
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiState
import com.mazarini.resa.ui.screens.locationsearch.state.MIN_LENGTH_FOR_SEARCH
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.fontSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
)
@Composable
fun LocationSearchScreen(
    locationSearchUiState: StateFlow<LocationSearchUiState>,
    onEvent: (LocationSearchUiEvent) -> Unit,
    navigateTo: (route: Route) -> Unit = {},
    upPress: () -> Unit = {},
) {

    val uiState by locationSearchUiState.collectAsStateWithLifecycle()
    val isComplete by remember {
        derivedStateOf { uiState.isSelectionComplete }
    }
    var wasComplete by remember { mutableStateOf(false) }
    val filtersUiState = uiState.filtersUiState
    val savedLocations = uiState.savedLocations
    val recentLocations = uiState.recentLocations
    val searchResults = fetchSearchResults(uiState)
    val currentSearchText by fetchCurrentSearchText(uiState)
    val shouldShowResults = shouldShowResults(uiState = uiState, currentSearchText)
    var showFilters by remember { mutableStateOf(false) }

    /** States */
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

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
                    filterDetail = getFilterDetailText(filtersUiState.filters),
                    onIconClicked = {
                        handleBackPress(
                            sheetState = modalSheetState,
                            scope = scope,
                            upPress = upPress,
                        )
                    },
                ) {
                    showFilters = !showFilters
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
        if (modalSheetState.isVisible) {
            LocalSoftwareKeyboardController.current?.hide()
        }
        BackHandler(modalSheetState.isVisible) {
            scope.launch { modalSheetState.hide() }
        }
    }

    if (showFilters) {
        ModalBottomSheet(
            modifier = Modifier,
            shape = RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp),
            sheetState = modalSheetState,
            scrimColor = MTheme.colors.surfaceBlur,
            dragHandle = null,
            tonalElevation = 8.dp,
            onDismissRequest = { showFilters = false }
        ) {
            JourneyFilter(
                uiState = filtersUiState,
                onEvent = { onEvent(FilterEvent(it)) },
                dismissFilters = {
                    scope.launch {
                        modalSheetState.hide()
                        showFilters = false
                    }
                },
            )
        }
    }
    LaunchedEffect(isComplete) {
        if (isComplete && !wasComplete) {
            onEvent(NavigateToResults)
            navigateTo(Route.JourneySelection)
        }
        wasComplete = isComplete
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
    val currentSearchType = uiState.currentSearchType
    val originSearchRes = uiState.originSearchRes.collectAsLazyPagingItems()
    val destSearchRes = uiState.destSearchRes.collectAsLazyPagingItems()

    return when (currentSearchType) {
        CurrentSearchType.ORIGIN -> originSearchRes
        CurrentSearchType.DESTINATION -> destSearchRes
    }
}

@Composable
fun fetchCurrentSearchText(uiState: LocationSearchUiState): State<String> {
    val currentSearchType = uiState.currentSearchType
    val originSelected = uiState.originSelected
    val destSelected = uiState.destSelected
    val originSearch = uiState.originSearch
    val destSearch = uiState.destSearch

    return remember(
        currentSearchType,
        originSelected,
        destSelected,
        originSearch,
        destSearch
    ) {
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
    val currentSearchType = uiState.currentSearchType
    val originSelected = uiState.originSelected
    val destSelected = uiState.destSelected

    return currentSearchText.length >= MIN_LENGTH_FOR_SEARCH
            &&
            when (currentSearchType) {
                CurrentSearchType.ORIGIN -> originSelected == null
                CurrentSearchType.DESTINATION -> destSelected == null
            }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun LocationSearchPreview() {
    val fakeFavoriteList = FakeFactory.locationList()
    ResaTheme {
        LocationSearchScreen(
            locationSearchUiState = MutableStateFlow(
                LocationSearchUiState(
                    originSearchRes = flowOf(PagingData.from(fakeFavoriteList)),
                    originSearch = "abc",
                )
            ),
            onEvent = {},
            navigateTo = {},
        )
    }
}
