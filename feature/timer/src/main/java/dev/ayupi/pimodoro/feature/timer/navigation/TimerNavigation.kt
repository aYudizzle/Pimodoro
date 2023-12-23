package dev.ayupi.pimodoro.feature.timer.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import dev.ayupi.pimodoro.feature.timer.TimerRoot

const val TIMER_GRAPH_ROUTE_PATTERN = "timer_graph"
const val timerRoute = "timer_route"
private const val DEEP_LINK_URI = "https://www.ayupi.dev/pimodoro/timer"

fun NavController.navigateToTimerGraph(navOptions: NavOptions? = null) {
    this.navigate(TIMER_GRAPH_ROUTE_PATTERN, navOptions)
}

fun NavController.navigateToTimer(navOptions: NavOptions? = null) {
    this.navigate(timerRoute, navOptions)
}

fun NavGraphBuilder.timerGraph(
    navigateToPomodoro: (Int) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = TIMER_GRAPH_ROUTE_PATTERN,
        startDestination = timerRoute
    ) {
        composable(
            route = timerRoute,
            deepLinks = listOf(
                navDeepLink { uriPattern = DEEP_LINK_URI }
            )
        ) {
            TimerRoot(navigateToPomodoro = navigateToPomodoro)
        }
        nestedGraphs()
    }
}