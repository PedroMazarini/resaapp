package com.resa.ui.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources.getSystem
import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.resa.R
import com.resa.global.fake.FakeFactory
import com.resa.ui.commoncomponents.SavedJourneyAdd
import com.resa.ui.commoncomponents.SavedJourneyItem
import com.resa.ui.commoncomponents.permissions.Common
import com.resa.ui.commoncomponents.permissions.location.LocationAction
import com.resa.ui.commoncomponents.permissions.location.RequestLocation
import com.resa.ui.screens.home.components.Departures
import com.resa.ui.screens.home.components.HomeMap
import com.resa.ui.screens.home.components.bars.HomeSearchBar
import com.resa.ui.screens.home.model.SavedJourneyState
import com.resa.ui.screens.home.state.HomeUiEvent
import com.resa.ui.screens.home.state.HomeUiState
import com.resa.ui.theme.MTheme
import com.resa.ui.theme.ResaTheme
import com.resa.ui.util.Previews
import com.resa.ui.util.fontSize

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()
val Int.density: Int get() = (this / getSystem().displayMetrics.density).toInt()

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit = {},
    navToLocationSearch: () -> Unit = {},
    navToJourneySelection: () -> Unit = {},
) {

    val lazyListState = rememberLazyListState()
    val searchBarOffset = calculateSearchOffeset(lazyListState)
    val navigateToJourneySelection by uiState.navigateToJourneySelection

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MTheme.colors.background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .graphicsLayer {
//                    translationY = searchBarOffset
                }
                .background(MTheme.colors.background)
                .padding(bottom = 4.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            HomeMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 28.dp),
                uiState = uiState,
            )
            HomeSearchBar(
                onSearchBarClicked = navToLocationSearch,
            )
        }

        Box {
            SavedJourneys(uiState, onEvent, navToLocationSearch)
        }

        Departures(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            homeUiState = uiState,
            onEvent = onEvent,
        )
    }

    if (navigateToJourneySelection) {
        navToJourneySelection()
        onEvent(HomeUiEvent.NavigationRequested)
    }
}

@Composable
fun SavedJourneys(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    navToLocationSearch: () -> Unit,
) {
    val savedJourneyState by uiState.savedJourneyState
    val requestGpsLocation by uiState.requestGpsLocation
    val hasCheckedPermission by uiState.hasCheckedPermission

    Column {
        Text(
            modifier = Modifier
                .padding(start = 24.dp)
                .padding(vertical = 16.dp),
            text = stringResource(id = R.string.saved),
            style = MTheme.type.secondaryText.fontSize(16.sp),
        )

        LazyRow(
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = spacedBy(8.dp),
        ) {

            if (savedJourneyState is SavedJourneyState.Loaded) {
                val loaded = savedJourneyState as SavedJourneyState.Loaded
                items(loaded.journeys) {
                    SavedJourneyItem(
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
                    SavedJourneyAdd(onAddClicked = navToLocationSearch)
                }
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

@Composable
fun calculateSearchOffeset(lazyListState: LazyListState): Float {
    val offsetChange by remember {
        derivedStateOf {
            (lazyListState.firstVisibleItemIndex == 0) &&
                    (lazyListState.firstVisibleItemScrollOffset.density < 106)
        }
    }
    return if (offsetChange)
        lazyListState.firstVisibleItemScrollOffset.toFloat().unaryMinus()
    else 106.px.toFloat().unaryMinus()
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Previews
fun HomeDefaultPreview() {
    ResaTheme {
        HomeScreen(
            uiState = HomeUiState(
                savedJourneyState = mutableStateOf(
                    SavedJourneyState.Loaded(
                        FakeFactory.journeySearchList()
                    )
                ),
            ),
        )
    }
}
