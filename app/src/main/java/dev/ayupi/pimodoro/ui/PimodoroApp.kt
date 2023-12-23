package dev.ayupi.pimodoro.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import dev.ayupi.designsystem.icon.PiIcons
import dev.ayupi.pimodoro.feature.settings.R as settingsR
import dev.ayupi.pimodoro.R
import dev.ayupi.pimodoro.feature.settings.SettingsRoot
import dev.ayupi.pimodoro.feature.timer.navigation.TIMER_GRAPH_ROUTE_PATTERN
import dev.ayupi.pimodoro.feature.timer.navigation.timerRoute
import dev.ayupi.pimodoro.navigation.PiNavHost
import dev.ayupi.pimodoro.navigation.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PimodoroApp(
    appState: PimAppState = rememberPimAppState()
) {

    val snackbarHostState = remember { SnackbarHostState() }

    var showSettingsDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (showSettingsDialog) {
        SettingsRoot(
            onDismiss = { showSettingsDialog = false }
        )
    }

    val currentDestination = appState.currentDestionation?.route

    Scaffold(
        topBar = {
            TopAppBar(title = {},actions = {
                if(currentDestination == timerRoute) {
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Icon(
                            imageVector = if (showSettingsDialog) PiIcons.Settings else PiIcons.SettingsBorder,
                            contentDescription = null
                        )
                    }
                }
            })
        }
    ) { padding ->
        PiNavHost(
            onShowSnackbar = { message, action ->
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = action,
                    duration = SnackbarDuration.Short
                ) == SnackbarResult.ActionPerformed
            },
            appState = appState,
            modifier = Modifier.padding(padding)
        )
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false