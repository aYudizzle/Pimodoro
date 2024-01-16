package dev.ayupi.feature.pomodoro

import PomodoroUiState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ayupi.feature.pomodoro.navigation.pomodoroArg
import dev.ayupi.pimodoro.core.data.repository.TimerManagerRepository
import dev.ayupi.pimodoro.core.manager.data.TimerState.Break
import dev.ayupi.pimodoro.core.manager.data.TimerState.Finished
import dev.ayupi.pimodoro.core.manager.data.TimerState.Idle
import dev.ayupi.pimodoro.core.manager.data.TimerState.Running
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val timerManagerRepository: TimerManagerRepository,
) : ViewModel() {
    init {
        timerManagerRepository.resetTimer()
    }

    private val interval = savedStateHandle.getStateFlow(key = pomodoroArg, initialValue = 1)

    val uiState = timerManagerRepository.timerData.map {
        when (it) {
            is Break -> {
                PomodoroUiState.Success(
                    fullDuration = it.fullDuration,
                    remainingTime = it.remainingTime,
                    formattedTime = it.formattedTime,
                    cycleCount = it.cycleCount,
                    timerState = it.stateName,
                    maxCycleCount = interval.value
                )
            }
            Finished -> PomodoroUiState.Finished
            Idle -> PomodoroUiState.NotStarted
            is Running -> {
                PomodoroUiState.Success(
                    fullDuration = it.fullDuration,
                    remainingTime = it.remainingTime,
                    formattedTime = it.formattedTime,
                    cycleCount = it.cycleCount,
                    timerState = it.stateName,
                    maxCycleCount = interval.value
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PomodoroUiState.NotStarted
    )

    fun startPomodoro() {
        timerManagerRepository.startPomodoro(interval.value)
    }

    fun stopPomodoro() {
        timerManagerRepository.stopTimer()
    }
}