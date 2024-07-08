package com.mazarini.resa.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.mazarini.resa.ui.application.rememberResaAppState
import com.mazarini.resa.ui.theme.MTheme

@Composable
fun NavigationHost() {
    val appState = rememberResaAppState()

    Scaffold { innerPadding ->
        NavHost(
            navController = appState.navController,
            startDestination = Route.Home,
            modifier = Modifier
                .padding(innerPadding)
                .background(MTheme.colors.background),
        ) {
            addAppNavGraph(
                navigateTo = appState::navigateTo,
                upPress = appState::upPress,
            )
        }
    }
}