package dev.ayupi.pimodoro.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import dev.ayupi.feature.pomodoro.navigation.navigateToPomodoro
import dev.ayupi.feature.pomodoro.navigation.pomodoroScreen
import dev.ayupi.pimodoro.feature.timer.navigation.TIMER_GRAPH_ROUTE_PATTERN
import dev.ayupi.pimodoro.feature.timer.navigation.navigateToTimer
import dev.ayupi.pimodoro.feature.timer.navigation.timerGraph
import dev.ayupi.pimodoro.ui.PimAppState

@Composable
fun PiNavHost(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    startDestination: String = TIMER_GRAPH_ROUTE_PATTERN,
    appState: PimAppState
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        timerGraph(
            navigateToPomodoro = navController::navigateToPomodoro,
        ) {
            pomodoroScreen(
                navigateToTimer = { appState.navigateToTimerSettings() }
            )
        }
    }
}