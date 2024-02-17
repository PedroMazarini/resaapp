package com.resa.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.resa.ui.navigation.HomeRoutes.HOME_START
import com.resa.ui.navigation.MainRoutes.HOME
import kotlin.reflect.KFunction1

fun NavGraphBuilder.addAppNavGraph(
    modifier: Modifier = Modifier,
    navToLocationSearch: () -> Unit,
    navToJourneySelection: () -> Unit,
    upPress: () -> Unit = {},
) {
    navigation(
        route = HOME.route,
        startDestination = HOME_START.route,
    ) {
        addHomeNavGraph(
            modifier = modifier,
            navToLocationSearch = navToLocationSearch,
            navToJourneySelection = navToJourneySelection,
            upPress = upPress,
        )
    }
}
