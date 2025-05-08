package com.mazarini.resa.ui.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mazarini.resa.ui.navigation.Route

@Stable
class ResaAppState(val navController: NavHostController) {
    fun upPress(map: Map<String, Any?> = mapOf()) {
        map.forEach {
            navController.previousBackStackEntry?.savedStateHandle?.set(it.key, it.value)
        }
        navController.navigateUp()
    }

    fun navigateTo(route: Route) {
        if (route is Route.Back) {
            upPress()
        } else {
            navController.navigate(route)
        }
    }
}

@Composable
fun rememberResaAppState(
    navController: NavHostController = rememberNavController(),
) = remember(navController) {
    ResaAppState(navController)
}

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}
