package dev.ayupi.feature.pomodoro

import PomodoroUiState
import PomodoroUiState.*
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import dev.ayupi.designsystem.components.PiBigIconButton
import dev.ayupi.designsystem.components.PiBigIconButtonWithoutBorder
import dev.ayupi.designsystem.icon.PiIcons
import dev.ayupi.feature.pomodoro.R.*

@Composable
fun PomodoroRoot(
    viewModel: PomodoroViewModel = hiltViewModel(),
    navigateToTimer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PomodoroScreen(
        state = uiState,
        navigateToTimer = navigateToTimer,
        onStartTimerClicked = viewModel::startPomodoro,
        onStopTimerClicked = viewModel::stopPomodoro
    )
}

@Composable
internal fun PomodoroScreen(
    state: PomodoroUiState,
    navigateToTimer: () -> Unit,
    onStartTimerClicked: () -> Unit,
    onStopTimerClicked: () -> Unit,
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
                        onClick = onStartTimerClicked,
                        icon = PiIcons.Play,
                        size = 200
                    )
                }
            }

            is Success -> {
                TimerPanel(
                    state = state,
                    navigateToTimer = navigateToTimer,
                    onStopTimerClicked = onStopTimerClicked
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.TimerPanel(
    state: Success,
    navigateToTimer: () -> Unit,
    onStopTimerClicked: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }
    val animatedProgress = animateFloatAsState(
        targetValue = state.remainingTime.toFloat() / state.fullDuration.toFloat(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = ""
    ).value

    if (showResetDialog) {
        ResetDialog(
            onDismiss = { showResetDialog = false },
            navigateToTimer = {
                onStopTimerClicked()
                navigateToTimer()
            },
        )
    }

    BackHandler {
        showResetDialog = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

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
                Text(state.timerState)
                Text(text = state.formattedTime)
                Text(text = "${state.cycleCount} / ${state.maxCycleCount}")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        PiBigIconButton(onClick = { showResetDialog = true }, icon = PiIcons.Reset, size = 48)
    }
}

@Composable
private fun ColumnScope.FinishedPanel(
    navigateToTimer: () -> Unit,
    state: Finished,
) {
    PiBigIconButtonWithoutBorder(
        onClick = { navigateToTimer() },
        icon = PiIcons.FinishBorder,
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