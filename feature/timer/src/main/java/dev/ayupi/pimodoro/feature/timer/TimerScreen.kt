package dev.ayupi.pimodoro.feature.timer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.ayupi.designsystem.components.PiBigIconButton
import dev.ayupi.designsystem.components.PiButton
import dev.ayupi.designsystem.icon.PiIcons

@Composable
internal fun TimerRoot(
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = hiltViewModel(),
    navigateToPomodoro: (Int) -> Unit
) {
    val timerSetupUiState by viewModel.timerSetupUiState.collectAsStateWithLifecycle()
    val intervalSetting by viewModel.intervalSetting.collectAsStateWithLifecycle()

    TimerScreen(
        modifier = modifier,
        timerSetupUiState = timerSetupUiState,
        intervalSetting = intervalSetting,
        onIntervalIncreasedClick = viewModel::increaseInterval,
        onIntervalDecreasedClick = viewModel::decreaseInterval,
        navigateToPomodoro = navigateToPomodoro
    )
}

@Composable
internal fun TimerScreen(
    modifier: Modifier,
    timerSetupUiState: TimerSetupUiState,
    intervalSetting: Int,
    onIntervalIncreasedClick: (interval: Int) -> Unit,
    onIntervalDecreasedClick: (interval: Int) -> Unit,
    navigateToPomodoro: (Int) -> Unit,
) {
    when (timerSetupUiState) {
        TimerSetupUiState.Loading -> Unit
        is TimerSetupUiState.Success -> {
            Column(
                modifier = modifier.fillMaxSize().padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Pimodoro Timer", style = MaterialTheme.typography.titleLarge)
                Image(
                    modifier = Modifier.padding(10.dp),
                    painter = painterResource(id = R.mipmap.pemodoro),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.8f)
                        .border(
                            BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        DisplayPanel(
                            intervalSetting = intervalSetting,
                            displayedTime = timerSetupUiState.minutes,
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SettingsPanel(
                            onIntervalDecreasedClick = onIntervalDecreasedClick,
                            onIntervalIncreasedClick = onIntervalIncreasedClick,
                            intervalSetting = intervalSetting
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))
                PiButton(onClick = {
                    navigateToPomodoro(intervalSetting)
                }) {
                    Text(text = "Start")
                }
            }
        }
    }
}

@Composable
internal fun ColumnScope.DisplayPanel(
    intervalSetting: Int,
    displayedTime: Int
) {
    Text(
        style = MaterialTheme.typography.titleLarge,
        text = if(intervalSetting < 2) "$intervalSetting Cycle" else "$intervalSetting Cycles"
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        style = MaterialTheme.typography.titleLarge,
        text = "$displayedTime minutes"
    )
}

@Composable
internal fun ColumnScope.SettingsPanel(
    intervalSetting: Int,
    onIntervalDecreasedClick: (interval: Int) -> Unit,
    onIntervalIncreasedClick: (interval: Int) -> Unit,
) {
    PiBigIconButton(
        onClick = { onIntervalIncreasedClick(intervalSetting) },
        icon = PiIcons.Add
    )
    Spacer(modifier = Modifier.height(10.dp))
    PiBigIconButton(
        onClick = { onIntervalDecreasedClick(intervalSetting) },
        icon = PiIcons.Subtract
    )
}