package dev.ayupi.feature.pomodoro

import PomodoroUiState
import PomodoroUiState.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.ayupi.designsystem.components.PiBigIconButtonWithoutBorder
import dev.ayupi.feature.pomodoro.R.*

@Composable
fun PomodoroRoot(
    viewModel: PomodoroViewModel = hiltViewModel(),
    navigateToTimer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PomodoroScreen(
        state = uiState,
        onTimerStateChanged = viewModel::onTimerSwitched,
        navigateToTimer = navigateToTimer
    )
}

@Composable
internal fun PomodoroScreen(
    state: PomodoroUiState,
    onTimerStateChanged: (TimerState) -> Unit,
    navigateToTimer: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        /**
         * Handles the different ScreenStates
         */
        when (state) {
            Loading -> Unit
            is Finished -> {
                AnimatedVisibility(visible = true) {
                    FinishedPanel(
                        navigateToTimer = navigateToTimer,
                        state = state
                    )
                }
            }

            is NotStarted -> {
                AnimatedVisibility(visible = true) {
                    PiBigIconButtonWithoutBorder(
                        onClick = { onTimerStateChanged(TimerState.ACTIVE) },
                        icon = state.timerState.unselectedIcon,
                        size = 200
                    )
                }
            }

            is Success -> {
                TimerPanel(
                    state = state,
                    onTimerStateChanged = onTimerStateChanged,
                    navigateToTimer = navigateToTimer
                )
            }
        }
    }
}


@Composable
private fun ColumnScope.TimerControlPanel(
    timerState: TimerState,
    onTimerStateChanged: (TimerState) -> Unit,
    onShowDialog: (Boolean) -> Unit,
) {
    Row {
        TimerState.entries.take(2).forEach {
            val active = timerState == it || TimerState.FINISHED == timerState
            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { onTimerStateChanged(it) },
                    enabled = !active
                ) {
                    Icon(
                        imageVector = if (active) it.selectedIcon else it.unselectedIcon,
                        contentDescription = stringResource(
                            id = it.iconDescriptionId
                        )
                    )
                }
                if (active) {
                    Text(text = stringResource(id = it.titleTextId))
                }
            }
        }
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { onShowDialog(true) },
                enabled = true
            ) {
                Icon(
                    imageVector = Icons.Rounded.RestartAlt,
                    contentDescription = stringResource(
                        id = string.reset
                    )
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.TimerPanel(
    state: Success,
    onTimerStateChanged: (TimerState) -> Unit,
    navigateToTimer: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var pauseProgress by remember { mutableFloatStateOf(0f) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = ""
    ).value

    val animatedPauseProgress = animateFloatAsState(
        targetValue = pauseProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = ""
    ).value

    LaunchedEffect(state.timeMillis) {
        progress = state.timeMillis / TIMER_CYCLE.toFloat()
    }

    LaunchedEffect(state.pauseMillis) {
        pauseProgress = state.pauseMillis / state.pauseTimeMaxValue.toFloat()
    }

    if (showResetDialog) {
        ResetDialog(
            onDismiss = { showResetDialog = false },
            navigateToTimer = navigateToTimer,
        )
    }

    if (state.timerState == TimerState.TIMEOUT) {
        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                trackColor = Color.Gray,
                progress = animatedPauseProgress
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = state.pauseTime)
            }
        }
    } else {
        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                trackColor = Color.Gray,
                progress = animatedProgress
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = state.time)
                Text(text = "${state.intervalCycleCount} of ${state.maxCycles}")
            }
        }
    }

    Text(text = state.timerState.toString())
    TimerControlPanel(
        timerState = state.timerState,
        onTimerStateChanged = onTimerStateChanged,
        onShowDialog = { showResetDialog = it }
    )
}

@Composable
private fun ColumnScope.FinishedPanel(
    navigateToTimer: () -> Unit,
    state: Finished,
) {
    PiBigIconButtonWithoutBorder(
        onClick = { navigateToTimer() },
        icon = state.timerState.unselectedIcon,
        size = 200,
        tint = Color.Green
    )
}

@Composable
private fun ResetDialog(
    onDismiss: () -> Unit,
    navigateToTimer: () -> Unit
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 40.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(string.reset_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text(text = stringResource(id = string.reset_dialog_text))
            }
        },
        dismissButton = {
            Text(
                text = stringResource(string.cancel_dialog_button_text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
        confirmButton = {
            Text(
                text = stringResource(string.reset_dialog_button_text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        onDismiss()
                        navigateToTimer()
                    },
            )
        },
    )
}