package com.mazarini.resa.ui.screens.locationsearch

import android.annotation.SuppressLint
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
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mazarini.resa.R
import com.mazarini.resa.global.extensions.isNotNull
import com.mazarini.resa.global.extensions.toggle
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.LocationItem
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.FiltersTopBar
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.JourneyFilter
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.getFilterDetailText
import com.mazarini.resa.ui.commoncomponents.loading.LinearLoading
import com.mazarini.resa.ui.model.Location
import com.mazarini.resa.ui.navigation.Route
import com.mazarini.resa.ui.screens.locationsearch.components.searchFields.SearchFields
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentSearchType
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.*
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiState
import com.mazarini.resa.ui.screens.locationsearch.state.MIN_LENGTH_FOR_SEARCH
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.fontSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
)
@Composable
fun LocationSearchScreen(
    uiState: LocationSearchUiState,
    onEvent: (LocationSearchUiEvent) -> Unit,
    navigateTo: (route: Route) -> Unit = {},
    upPress: () -> Unit = {},
) {

    val isSelectionComplete by uiState.isSelectionComplete.collectAsState()
    val filtersUiState = uiState.filtersUiState
    val savedLocations by uiState.savedLocations
    val recentLocations by uiState.recentLocations
    val searchResults = fetchSearchResults(uiState)
    val currentSearchText by fetchCurrentSearchText(uiState)
    val shouldShowResults = shouldShowResults(uiState = uiState, currentSearchText)
    val showFilters = remember { mutableStateOf(false) }

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
                    filterDetail = getFilterDetailText(filtersUiState.filters.value),
                    onIconClicked = {
                        handleBackPress(
                            sheetState = modalSheetState,
                            scope = scope,
                            upPress = upPress,
                        )
                    },
                ) {
                    showFilters.toggle()
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
                uiState = filtersUiState,
                onEvent = { onEvent(FilterEvent(it)) },
                dismissFilters = {
                    scope.launch {
                        modalSheetState.hide()
                        showFilters.value = false
                    }
                },
            )
        }
    }
    if (isSelectionComplete) {
        onEvent(NavigateToResults)
        navigateTo(Route.JourneySelection)
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
        skipPartiallyExpanded = false,
        density = LocalDensity.current,
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
            navigateTo = {},
        )
    }
}
