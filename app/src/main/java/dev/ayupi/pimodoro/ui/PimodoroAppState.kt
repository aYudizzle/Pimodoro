package dev.ayupi.pimodoro.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.util.trace
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import dev.ayupi.pimodoro.feature.timer.navigation.navigateToTimer
import dev.ayupi.pimodoro.feature.timer.navigation.navigateToTimerGraph
import dev.ayupi.pimodoro.feature.timer.navigation.timerRoute
import dev.ayupi.pimodoro.navigation.TopLevelDestination
import dev.ayupi.pimodoro.navigation.TopLevelDestination.TIMER
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberPimAppState(
    navController: NavHostController = rememberNavController(),
    scope: CoroutineScope = rememberCoroutineScope()
): PimAppState {
    return remember(
        navController,
        scope
    ) {
        PimAppState(
            navController,
            scope
        )
    }
}

data class PimAppState(
    val navController: NavHostController,
    val scope: CoroutineScope,
) {
    val currentDestionation: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestionation?.route) {
            timerRoute -> TIMER
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                //Whether this navigation action should launch as single-top
                // (i.e., there will be at most one copy of
                // a given destination on the top of the back stack)
                launchSingleTop = true
                // Whether this navigation action should restore any state previously saved by
                restoreState = true
            }

            when (topLevelDestination) {
                TIMER -> navController.navigateToTimerGraph(topLevelNavOptions)
                // extendable
            }
        }
    }

    fun navigateToTimerSettings() {
        val navOptions = navOptions {
            launchSingleTop = true
            popUpTo(timerRoute)
        }
        navController.navigateToTimer(navOptions)
    }

}