package com.resa.ui.navigation

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.resa.ui.navigation.HomeRoutes.*
import com.resa.ui.screens.home.HomeScreen
import com.resa.ui.screens.home.state.HomeViewModel
import com.resa.ui.screens.journeydetails.JourneyDetailsScreen
import com.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.resa.ui.screens.journeydetails.state.JourneyDetailsViewModel
import com.resa.ui.screens.journeyselection.JourneySelectionScreen
import com.resa.ui.screens.journeyselection.state.JourneySelectionViewModel
import com.resa.ui.screens.locationsearch.LocationSearchScreen
import com.resa.ui.screens.locationsearch.state.LocationSearchViewModel

fun NavGraphBuilder.addHomeNavGraph(
    modifier: Modifier = Modifier,
    navToLocationSearch: () -> Unit,
    navToJourneySelection: () -> Unit,
    navToJourneyDetails: () -> Unit,
    upPress: () -> Unit = {},
) {
    composable(HOME_START.route) {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(
            uiState = viewModel.uiState,
            onEvent = viewModel::onEvent,
            navToJourneySelection = navToJourneySelection,
            navToLocationSearch = navToLocationSearch,
        )
    }
    composable(HOME_LOCATION_SEARCH.route) {

        val viewModel = hiltViewModel<LocationSearchViewModel>()
        viewModel.verifyQueryFilters()
        LocationSearchScreen(
            uiState = viewModel.uiState,
            onEvent = viewModel::onEvent,
            navToJourneySelection = navToJourneySelection,
            upPress = upPress,
        )
    }
    composable(HOME_JOURNEY_SELECTION.route) {
        val viewModel = hiltViewModel<JourneySelectionViewModel>()
        JourneySelectionScreen(
            uiState = viewModel.uiState,
            onEvent = viewModel::onEvent,
            navigateToLocationSearch = {},
            navigateToJourneyDetails = navToJourneyDetails,
            upPress = upPress,
        )
    }
    composable(HOME_JOURNEY_DETAILS.route) {
        val viewModel = hiltViewModel<JourneyDetailsViewModel>()

        JourneyDetailsScreen(
            uiState = viewModel.uiState,
            onEvent = {event ->
                if (event is JourneyDetailsUiEvent.OnBackPressed) {
                    upPress()
                } else {
                    viewModel.onEvent(event)
                }
            },
        )
    }
}
