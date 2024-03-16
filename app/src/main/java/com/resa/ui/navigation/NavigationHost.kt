package com.resa.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.resa.ui.application.rememberResaAppState
import com.resa.ui.navigation.MainRoutes.HOME
import com.resa.ui.theme.MTheme

@Composable
fun NavigationHost() {
    val appState = rememberResaAppState()

    Scaffold { innerPadding ->
        NavHost(
            navController = appState.navController,
            startDestination = HOME.route,
            modifier = Modifier
                .padding(innerPadding)
                .background(MTheme.colors.background)
        ) {
            addAppNavGraph(
                navToLocationSearch = appState::navToLocationSearch,
                navToJourneySelection = appState::navToJourneySelection,
                navToJourneyDetails = appState::navToJourneyDetails,
                upPress = appState::upPress,
            )
        }
    }
}