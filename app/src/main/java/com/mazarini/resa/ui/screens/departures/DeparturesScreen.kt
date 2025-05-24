package com.mazarini.resa.ui.screens.departures

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun DeparturesScreen(
    departuresUiState: StateFlow<DeparturesUiState>,
    onEvent: (DeparturesUiEvent) -> Unit,
    navigateTo: (route: Route) -> Unit,
) {
    val uiState by departuresUiState.collectAsStateWithLifecycle()

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

        if (uiState.isLoading) {
            LoadingStops()
        } else {
            uiState.selectedStop?.let {
                DeparturesResultList(departures = uiState.departures, onEvent = onEvent)
            } ?: run {
                if (uiState.showStopQueryResult) {
                    StopQueryResultList(
                        stopQuery = uiState.stopQuery,
                        stopQueryResult = uiState.stopQueryResult,
                        onEvent = onEvent
                    )
                }
            }
        }
    }

    if (uiState.userLocationRequest is CurrentLocation.Request) {
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
            departuresUiState = MutableStateFlow(
                DeparturesUiState(
                    stopQueryResult = FakeFactory.locationList()
                )
            ),
            onEvent = {},
            navigateTo = {},
        )
    }
}