package com.mazarini.resa.ui.navigation

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mazarini.resa.ui.screens.about.AboutScreen
import com.mazarini.resa.ui.screens.departures.DeparturesScreen
import com.mazarini.resa.ui.screens.departures.state.DeparturesViewModel
import com.mazarini.resa.ui.screens.home.HomeScreen
import com.mazarini.resa.ui.screens.home.state.HomeViewModel
import com.mazarini.resa.ui.screens.journeydetails.JourneyDetailsScreen
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsViewModel
import com.mazarini.resa.ui.screens.journeyselection.JourneySelectionScreen
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionViewModel
import com.mazarini.resa.ui.screens.locationsearch.LocationSearchScreen
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchViewModel
import com.mazarini.resa.ui.screens.onboarding.OnboardingScreen

fun NavGraphBuilder.addAppNavGraph(
    upPress: () -> Unit = {},
    navigateTo: (route: Route) -> Unit = {},
) {
    composable<Route.Home> {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(
            uiState = viewModel.uiState,
            onEvent = viewModel::onEvent,
            navigateTo = navigateTo,
        )
    }
    composable<Route.LocationSearch> {
        val viewModel = hiltViewModel<LocationSearchViewModel>()
        viewModel.verifyQueryFilters()
        LocationSearchScreen(
            uiState = viewModel.uiState,
            onEvent = viewModel::onEvent,
            navigateTo = navigateTo,
            upPress = upPress,
        )
    }
    composable<Route.JourneySelection> {
        val viewModel = hiltViewModel<JourneySelectionViewModel>()
        JourneySelectionScreen(
            uiState = viewModel.uiState,
            onEvent = viewModel::onEvent,
            navigateTo = navigateTo,
            upPress = upPress,
        )
    }
    composable<Route.JourneyDetails> {
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
    composable<Route.Departures> {
        val viewModel = hiltViewModel<DeparturesViewModel>()

        DeparturesScreen(
            uiState = viewModel.uiState,
            onEvent = viewModel::onEvent,
            navigateTo = navigateTo,
        )
    }
    composable<Route.About> {
        AboutScreen(
            modifier = Modifier,
            navigateTo = navigateTo,
        )
    }
    composable<Route.Onboarding> {
        OnboardingScreen(
            modifier = Modifier,
            navigateTo = navigateTo,
        )
    }
}
