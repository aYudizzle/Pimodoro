package dev.ayupi.feature.pomodoro.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.ayupi.feature.pomodoro.PomodoroRoot

//const val pomodoroRoute = "pomodoro_route/{interval}"
private const val DEEP_LINK_URI = "https://www.ayupi.dev/pimodoro/pomodoro/1"

internal const val pomodoroArg = "interval"

fun NavController.navigateToPomodoro(interval: Int, navOptions: NavOptions? = null){
    this.navigate("pomodoro_route/$interval", navOptions)
}

fun NavGraphBuilder.pomodoroScreen(navigateToTimer: () -> Unit) {
    composable(
        route = "pomodoro_route/{$pomodoroArg}",
        arguments = listOf(
            navArgument(pomodoroArg) { type = NavType.IntType }
        ),
    ) {
        PomodoroRoot(
            navigateToTimer = navigateToTimer
        )
    }
}