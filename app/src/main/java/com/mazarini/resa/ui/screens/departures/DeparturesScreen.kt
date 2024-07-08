package com.mazarini.resa.ui.screens.departures

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mazarini.resa.R
import com.mazarini.resa.domain.model.Coordinate
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.commoncomponents.permissions.location.LocationAction
import com.mazarini.resa.ui.commoncomponents.permissions.location.RequestLocation
import com.mazarini.resa.ui.navigation.Route
import com.mazarini.resa.ui.screens.departures.components.DepartureSearchField
import com.mazarini.resa.ui.screens.departures.components.DeparturesResultList
import com.mazarini.resa.ui.screens.departures.components.LoadingStops
import com.mazarini.resa.ui.screens.departures.components.RecentStopChipList
import com.mazarini.resa.ui.screens.departures.components.StopQueryResultList
import com.mazarini.resa.ui.screens.departures.components.TopBar
import com.mazarini.resa.ui.screens.departures.state.DeparturesUiEvent
import com.mazarini.resa.ui.screens.departures.state.DeparturesUiState
import com.mazarini.resa.ui.screens.locationsearch.state.CurrentLocation
import com.mazarini.resa.ui.theme.ResaTheme

@Composable
fun DeparturesScreen(
    uiState: DeparturesUiState,
    onEvent: (DeparturesUiEvent) -> Unit,
    navigateTo: (route: Route) -> Unit,
) {
    val locationRequest by remember { uiState.userLocationRequest }
    val isLoading by remember { uiState.isLoading }
    val selectedStop by uiState.selectedStop.collectAsState()
    val showStopQueryResult by remember { uiState.showStopQueryResult }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            navigateTo = navigateTo,
        )
        DepartureSearchField(
            modifier = Modifier,
            uiState = uiState,
            onEvent = onEvent,
        )

        RecentStopChipList(uiState = uiState, onEvent = onEvent)

        if (isLoading) {
            LoadingStops()
        } else {
            selectedStop?.let {
                DeparturesResultList(uiState = uiState, onEvent = onEvent)
            } ?: run {
                if (showStopQueryResult) {
                    StopQueryResultList(uiState, onEvent)
                }
            }
        }
    }

    if (locationRequest is CurrentLocation.Request) {
        onEvent(DeparturesUiEvent.UserLocationRequest(CurrentLocation.Loading))
        RequestLocationPermission(onEvent)
    }
}

@Composable
private fun RequestLocationPermission(onEvent: (DeparturesUiEvent) -> Unit) {
    val locationFailed = Toast.makeText(
        LocalContext.current,
        stringResource(R.string.location_failed),
        Toast.LENGTH_LONG,
    )
    val myLocationName = stringResource(id = R.string.my_location)
    RequestLocation {
        onEvent(DeparturesUiEvent.UserLocationRequest(null))
        when (it) {
            is LocationAction.OnSuccess -> {
                onEvent(
                    DeparturesUiEvent.QueryByCoordinate(
                        Coordinate(lat = it.lat, lon = it.lon),
                        myLocationName
                    )
                )
            }
            is LocationAction.OnPermissionRequested -> {
                onEvent(DeparturesUiEvent.LocationRequestOngoing)
            }
            else -> {
                locationFailed.show()
                onEvent(DeparturesUiEvent.LocationRequestOngoing)
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun DeparturesScreenPreview() {
    ResaTheme {
        DeparturesScreen(
            uiState = DeparturesUiState(
                stopQueryResult = mutableStateOf(FakeFactory.locationList())
            ),
            onEvent = {},
            navigateTo = {},
        )
    }
}