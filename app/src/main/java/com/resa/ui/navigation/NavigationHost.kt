package com.resa.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.resa.ui.application.rememberResaAppState
import com.resa.ui.navigation.MainRoutes.*
import com.resa.ui.theme.MTheme

@Composable
fun NavigationHost() {
    val appState = rememberResaAppState()

    Scaffold { innerPadding ->
        val toast = Toast.makeText(LocalContext.current, "Action", Toast.LENGTH_LONG)
        NavHost(
            navController = appState.navController,
            startDestination = HOME.route,
            modifier = Modifier
                .padding(innerPadding)
                .background(MTheme.colors.background)
        ) {
            addAppNavGraph(
                onFavClicked = { toast.show() },
                navToLocationSearch = appState::navToLocationSearch,
                navToJourneySelection = appState::navToJourneySelection,
                upPress = appState::upPress,
            )
        }
    }
}