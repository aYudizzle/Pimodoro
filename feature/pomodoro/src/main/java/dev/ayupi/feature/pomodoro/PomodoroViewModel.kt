package dev.ayupi.feature.pomodoro

import PomodoroUiState
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ayupi.pimodoro.core.manager.data.SoundManager
import dev.ayupi.feature.pomodoro.navigation.pomodoroArg
import dev.ayupi.pimodoro.core.data.repository.TimerManagerRepository
import dev.ayupi.pimodoro.core.data.repository.UserDataRepository
import dev.ayupi.pimodoro.core.domain.PlayPauseSoundUseCase
import dev.ayupi.pimodoro.core.domain.PlayResumeSoundUseCase
import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userDataRepository: UserDataRepository,
    private val playPauseSoundUseCase: PlayPauseSoundUseCase,
    private val playResumeSoundUseCase: PlayResumeSoundUseCase,
    private val timerManagerRepository: TimerManagerRepository,
) : ViewModel() {

    private val interval = savedStateHandle.getStateFlow(key = pomodoroArg, initialValue = 1)
    private val time = savedStateHandle.getStateFlow(key = TIME_KEY, initialValue = 0L)
    private val timeout = savedStateHandle.getStateFlow(key = TIME_TIMEOUT_KEY, initialValue = 0L)
    private val pauseTime = savedStateHandle.getStateFlow(key = PAUSE_TIME_KEY, initialValue = 1L)
    private val activeState =
        savedStateHandle.getStateFlow(key = TIMER_STATUS_KEY, initialValue = TimerState.NOT_STARTED)

    private val breakTimeConfig = userDataRepository.userData.map {
        it.preferredBreakTime
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BreakTimeConfig.QUARTER
    )

    val otherState = timerManagerRepository.timerData.map {
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = dev.ayupi.pimodoro.core.manager.data.TimerState.Idle
    )

    // represents the uiState
    val uiState: StateFlow<PomodoroUiState> = activeState.flatMapLatest { activeStateValue ->
        time.flatMapLatest { timeValue ->
            interval.flatMapLatest { interval ->
                timeout.flatMapLatest { pauseTimer ->
                    pauseTime.map { pauseTimeValue ->
                        when (activeStateValue) {
                            TimerState.NOT_STARTED -> {
                                PomodoroUiState.NotStarted(activeStateValue)
                            }
                            TimerState.FINISHED -> {
                                PomodoroUiState.Finished(activeStateValue)
                            }
                            else -> {
                                PomodoroUiState.Success(
                                    timerState = activeStateValue,
                                    time = formatTime(timeValue),
                                    timeMillis = timeValue,
                                    pauseMillis = pauseTimer,
                                    pauseTime = formatTime(pauseTimer),
                                    intervalCycleCount = actualCycle,
                                    maxCycles = interval,
                                    pauseTimeMaxValue = pauseTimeValue
                                )
                            }
                        }
                    }
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PomodoroUiState.Loading
    )


    // state of the last timer for the control of the
    // timer behaviour
    private var job: Job? = null
    private var actualCycle = 1

    // function to switch between timer states
    fun onTimerSwitched(timerState: TimerState) {
        when (timerState) {
            TimerState.ACTIVE -> setTimerState(timerState)
            TimerState.PAUSE -> setTimerState(timerState)
            TimerState.TIMEOUT -> setTimerState(timerState)
            else -> Unit
        }
    }

    private fun setTimerState(timerState: TimerState) {
        savedStateHandle[TIMER_STATUS_KEY] = timerState
        handleTimerStateChange()
    }

    private fun handleTimerStateChange() {
        when (activeState.value) {
            TimerState.ACTIVE -> startTimer()
            TimerState.PAUSE -> job?.cancel()
            TimerState.TIMEOUT -> pauseTimer()
            else -> Unit
        }
    }

    private fun pauseTimer() {
        job = viewModelScope.launch {
            launch { playSignal() }
            savedStateHandle[TIME_KEY] = 0
            runningPauseTimer()
        }
    }

    private suspend fun runningPauseTimer() {
        savedStateHandle[PAUSE_TIME_KEY] = getTimeoutDuration()
        while (activeState.value == TimerState.TIMEOUT && timeout.value != pauseTime.value) {
            delay(INCREASE_TIME)
            savedStateHandle[TIME_TIMEOUT_KEY] = timeout.value + INCREASE_TIME
        }
        setTimerState(TimerState.ACTIVE)
        savedStateHandle[TIME_TIMEOUT_KEY] = 0L
    }

    private fun getTimeoutDuration(): Long {
        if (actualCycle % 5 == 0) {
            return 300_000
        }
        return when (breakTimeConfig.value) {
            BreakTimeConfig.QUARTER -> 9_000L//900_000
            BreakTimeConfig.HALF -> 12_000L//1_800_000
        }
    }

    /**
     *  starts the timer if the previous state was INACTIVE it resets the timer as well
     *  else it should only continue running
     */
    private fun startTimer() {
        timerManagerRepository.startPomodoro(2)
        job = viewModelScope.launch {
            launch { playSignal() }
            runTimer()
        }
    }

    /**
     * actual timer logic - checks if the timerState is Active and the Time value isn't reached yet.
     * while true it delays the timer 1000 millis and increases the timer for that given time.
     */
    private suspend fun runTimer() {
        while (activeState.value == TimerState.ACTIVE && time.value != TIMER_CYCLE) {
            delay(1000L)
            savedStateHandle[TIME_KEY] = time.value + 1000
        }
        handleCycleChange()
    }

    /**
     * checks if the cycle was the last cycle to go
     * else it change the state to timeout instead of inactive
     */
    private fun handleCycleChange() {
//        Log.i("STATE", "${otherState.value}")
        Log.i("TimeoutDuration", "cycle = $actualCycle")
        if (actualCycle < interval.value) {
            actualCycle++
            setTimerState(TimerState.TIMEOUT)
        } else {
            setTimerState(TimerState.FINISHED)
        }
    }

    /**
     * formats the output time to mm:ss
     */
    private fun formatTime(timeMillis: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeMillis),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofPattern(
            "mm:ss",
            Locale.getDefault()
        )
        return localDateTime.format(formatter)
    }

    private suspend fun playSignal() {
        if(activeState.value == TimerState.TIMEOUT) {
            playPauseSoundUseCase.execute()
        } else {
            playResumeSoundUseCase.execute()
        }

    }
}

const val TIMER_CYCLE = 5_000L//1_500_000L
const val TIMER_STATUS_KEY = "active_state"
const val TIME_TIMEOUT_KEY = "timeout_time_value"
const val TIME_KEY = "time_value"
const val PAUSE_TIME_KEY = "pause_time_value"
const val INCREASE_TIME = 1000L