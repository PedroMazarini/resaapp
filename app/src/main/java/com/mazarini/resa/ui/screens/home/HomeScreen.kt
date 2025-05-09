package com.mazarini.resa.ui.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mazarini.resa.R
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.permissions.Common
import com.mazarini.resa.ui.commoncomponents.permissions.location.LocationAction
import com.mazarini.resa.ui.commoncomponents.permissions.location.RequestLocation
import com.mazarini.resa.ui.navigation.Route
import com.mazarini.resa.ui.screens.home.components.NavigationDrawer
import com.mazarini.resa.ui.screens.home.components.RecentJourneySearchItem
import com.mazarini.resa.ui.screens.home.components.SavedHomeJourneyCard
import com.mazarini.resa.ui.screens.home.components.SavedJourneyAdd
import com.mazarini.resa.ui.screens.home.components.SavedJourneyItem
import com.mazarini.resa.ui.screens.home.components.bars.HomeSearchBar
import com.mazarini.resa.ui.screens.home.model.SavedJourneyState
import com.mazarini.resa.ui.screens.home.state.HomeUiEvent
import com.mazarini.resa.ui.screens.home.state.HomeUiState
import com.mazarini.resa.ui.screens.home.util.CollapsingAppBarNestedScrollConnection
import com.mazarini.resa.ui.theme.MTheme
import com.mazarini.resa.ui.theme.ResaTheme
import com.mazarini.resa.ui.util.Previews
import com.mazarini.resa.ui.util.fontSize
import com.mazarini.resa.ui.util.onHeightChanged
import com.mazarini.resa.ui.util.pxToDp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    homeUiState: StateFlow<HomeUiState>,
    onEvent: (HomeUiEvent) -> Unit = {},
    navigateTo: (route: Route) -> Unit = {},
) {

    val uiState by homeUiState.collectAsStateWithLifecycle()
    val showOnboarding by remember { derivedStateOf { uiState.showOnboarding } }
    val wasOnboardingSeen by remember { mutableStateOf(false) }
    val navigateToSelection by remember { derivedStateOf { uiState.navigateToJourneySelection } }
    var navigateRequested by remember { mutableStateOf(false) }
    var headerHeightPx by remember {
        mutableFloatStateOf(0f)
    }
    val connection = remember(headerHeightPx) {
        CollapsingAppBarNestedScrollConnection(headerHeightPx)
    }
    val density = LocalDensity.current
    val spaceHeightDp = remember(connection.headerOffset) {
        derivedStateOf {
            with(density) {
                (headerHeightPx + connection.headerOffset).toDp()
            }
        }
    }
    val scrollState = rememberLazyListState()
    remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }.also {
        connection.shouldScroll.value = it.value
    }

    Box(
        modifier = Modifier
            .background(MTheme.colors.background)
            .nestedScroll(connection)
            .fillMaxSize(),
    ) {
        NavigationDrawer(
            modifier = Modifier,
            yOffsetDp = connection.headerOffset.pxToDp(),
            currentLanguage = uiState.currentLanguage,
            currentTheme = uiState.currentTheme,
            onEvent = onEvent,
            navigateTo = navigateTo,
        ) {
            Box(
                modifier = Modifier
                    .offset(y = connection.headerOffset.pxToDp())
                    .zIndex(1f)
                    .background(MTheme.colors.background)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .onHeightChanged {
                        headerHeightPx = it.toFloat()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 16.dp),
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f)
                            .padding(start = 24.dp),
                        painter = painterResource(id = R.drawable.home_bg),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                    )
                    HomeSearchBar(
                        modifier = Modifier.offset(y = (connection.headerOffset * 0.3f).pxToDp()),
                        onSearchBarClicked = { navigateTo(Route.LocationSearch()) },
                        onTimeTableClicked = { navigateTo(Route.Departures) }
                    )
                }
            }

            SavedJourneys(
                uiState,
                onEvent,
                navigateTo,
                spaceHeightDp.value,
                scrollState
            )
        }
    }

    LaunchedEffect(Unit) {
        onEvent(HomeUiEvent.CheckSavedJourneyToHome)
    }
    LaunchedEffect(navigateToSelection) {
        if (navigateToSelection && !navigateRequested) {
            onEvent(HomeUiEvent.NavigationRequested)
            navigateTo(Route.JourneySelection)
        }
        navigateRequested = navigateToSelection
    }
    LaunchedEffect(showOnboarding) {
        if (showOnboarding && !wasOnboardingSeen) {
            onEvent(HomeUiEvent.OnboardShown)
            navigateTo(Route.Onboarding)
        }
    }
}

@Composable
fun SavedJourneys(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    navigateTo: (route: Route) -> Unit = {},
    contentPadding: Dp,
    scrollState: LazyListState,
) {
    val savedJourneyState = uiState.savedJourneyState
    val requestGpsLocation = uiState.requestGpsLocation
    val hasCheckedPermission = uiState.hasCheckedPermission
    val recentJourneysState = uiState.recentJourneysState
    val pinnedHomeJourney = uiState.pinnedHomeJourney
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth * 0.55f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = contentPadding),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = spacedBy(8.dp),
            state = scrollState,
        ) {

            pinnedHomeJourney?.let {
                item {
                    SavedHomeJourneyCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        journey = it,
                        onEvent = onEvent,
                        navigateTo = navigateTo,
                    )
                }
            }
            item {
                Text(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .padding(vertical = 16.dp),
                    text = stringResource(id = R.string.saved),
                    style = MTheme.type.secondaryText.fontSize(16.sp),
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {

                    if (savedJourneyState is SavedJourneyState.Loaded) {
                        val loaded = savedJourneyState as SavedJourneyState.Loaded
                        items(loaded.journeys) {
                            SavedJourneyItem(
                                modifier = Modifier.width(itemWidth),
                                journeySearch = it,
                                showDeleteButton = true,
                                onItemClicked = { journeySearch ->
                                    onEvent(HomeUiEvent.OnSavedJourneyClicked(journeySearch))
                                },
                                onDelete = { id ->
                                    onEvent(HomeUiEvent.DeleteSavedJourney(id))
                                },
                            )
                        }
                    } else {
                        item {
                            SavedJourneyAdd(onAddClicked = { navigateTo(Route.LocationSearch()) })
                        }
                    }
                }
            }

            if (recentJourneysState is SavedJourneyState.Loaded) {
                recentJourneys(recentJourneysState as SavedJourneyState.Loaded, onEvent)
            }
        }
    }
    if (requestGpsLocation) {
        RequestLocationPermission(onEvent)
    }
    if (!hasCheckedPermission) {
        CheckLocationPermission(onEvent)
    }
}

fun LazyListScope.recentJourneys(
    recentJourneys: SavedJourneyState.Loaded,
    onEvent: (HomeUiEvent) -> Unit
) {
    item {
        Text(
            modifier = Modifier
                .padding(start = 24.dp)
                .padding(top = 16.dp, bottom = 8.dp),
            text = stringResource(id = R.string.recent),
            style = MTheme.type.secondaryText.fontSize(16.sp),
        )
    }
    items((recentJourneys).journeys) {
        RecentJourneySearchItem(
            journeySearch = it,
            onEvent = onEvent,
        )
    }
}

@Composable
fun CheckLocationPermission(onEvent: (HomeUiEvent) -> Unit) {

    val hasPermission = Common.checkIfPermissionGranted(
        LocalContext.current,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    onEvent(HomeUiEvent.CheckedPermission(hasPermission))
}

@Composable
fun RequestLocationPermission(onEvent: (HomeUiEvent) -> Unit) {
    val locationFailed = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.location_failed),
        Toast.LENGTH_LONG,
    )
    RequestLocation {
        onEvent(HomeUiEvent.ClearLoadingSavedJourneys)
        when (it) {
            is LocationAction.OnSuccess -> {
                onEvent(HomeUiEvent.UpdateGpsRequest(false))
                onEvent(HomeUiEvent.LocationResult(lat = it.lat, lon = it.lon))
            }

            else -> {
                onEvent(HomeUiEvent.UpdateGpsRequest(false))
                locationFailed.show()
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Previews
fun HomeDefaultPreview() {
    ResaTheme {
        HomeScreen(
            homeUiState = MutableStateFlow(
                HomeUiState(
                    savedJourneyState = SavedJourneyState.Loaded(FakeFactory.journeySearchList()),
                    recentJourneysState = SavedJourneyState.Loaded(FakeFactory.journeySearchList())
                )
            ),
        )
    }
}
