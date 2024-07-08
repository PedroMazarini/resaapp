package com.mazarini.resa.ui.application

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mazarini.resa.ui.navigation.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class ResaAppState(
    val snackbarHostState: SnackbarHostState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope,
) {
    // Process snackbars coming from SnackbarManager
    init {
        coroutineScope.launch {
            SnackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val message = currentMessages[0]
                    val text = resources.getText(message.messageId)

                    // Display the snackbar on the screen. `showSnackbar` is a function
                    // that suspends until the snackbar disappears from the screen
                    snackbarHostState.showSnackbar(text.toString())
                    // Once the snackbar is gone or dismissed, notify the SnackbarManager
                    SnackbarManager.setMessageShown(message.id)
                }
            }
        }
    }

    private val currentRoute: String
        get() = navController.currentDestination?.route ?: ""

    fun upPress(map: Map<String, Any?> = mapOf()) {
        map.forEach {
            navController.previousBackStackEntry?.savedStateHandle?.set(it.key, it.value)
        }
        navController.navigateUp()
    }

    fun navigateToAppSection(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
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
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
    ResaAppState(snackbarHostState, navController, snackbarManager, resources, coroutineScope)
}

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Copied from similar function in NavigationUI.kt
 *
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
 */
private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}

@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}