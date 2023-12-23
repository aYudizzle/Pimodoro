package dev.ayupi.pimodoro.core.manager.data

import android.os.CountDownTimer
import android.util.Log
import dev.ayupi.pimodoro.core.datastore.PiPreferencesDataStore
import dev.ayupi.pimodoro.core.manager.model.TimerConfigEntity
import dev.ayupi.pimodoro.core.manager.model.toTimerConfigEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimerManagerImpl @Inject constructor(
    private val piPreferencesDataStore: PiPreferencesDataStore
) : TimerManager {
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val pomodoroBeforeLongBreak = 4

    private val _timerStateFlow = MutableStateFlow<TimerState>(TimerState.Idle)
    override val timerStateFlow: Flow<TimerState> get() = _timerStateFlow.asStateFlow()

    private var currentTimer: CountDownTimer? = null
    private var pomodoroCycleCount = 0
    private var maxCycleCount: Int = 0

    private var duration: Long = 0L

    private var breakTimeConfig: TimerConfigEntity = TimerConfigEntity.HALF


//    private val offset = 1000L // 1000ms offset so the timer can reach 0

    override fun startPomodoro(maxCycleCount: Int) {
        Log.i("Timer: ", "Started with $maxCycleCount")
        scope.launch {
            piPreferencesDataStore.userData.collect {
                breakTimeConfig = it.preferredBreakTime.toTimerConfigEntity()
            }
        }
        this.maxCycleCount = maxCycleCount
        startCycle()
    }

    override fun startBreak() {
        val breakDuration = if (pomodoroCycleCount % pomodoroBeforeLongBreak != 0) {
            TimerConfigEntity.SHORT.ms
        } else breakTimeConfig.ms

        Log.i("Timer: ", "Break with ${ breakDuration/ 60 / 1000 } Minuten")
        startTimer(TimerState.Break(breakDuration))
    }

    fun startCycle() {
        pomodoroCycleCount++
        Log.i("Timer: ","Start von Cycle $pomodoroCycleCount")
        val cycleDuration = TimerConfigEntity.CYCLE.ms
        startTimer(TimerState.Running(cycleDuration))
    }

    override fun stopTimer() {
        currentTimer?.cancel()
    }

    private fun startTimer(timerType: TimerState) {
        stopTimer() //if a timer is already running stop the timer before
        duration = when (timerType) {
            is TimerState.Running -> timerType.remainingTime //+ offset
            is TimerState.Break -> timerType.remainingTime
            else -> 0L
        }

//        Log.i("TIMER: ", "STARTED")
        currentTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val updatedState = if (timerType is TimerState.Running) {
                    TimerState.Running(millisUntilFinished)
                } else {
                    TimerState.Break(millisUntilFinished)
                }
                _timerStateFlow.value = updatedState
            }

            override fun onFinish() {
                if (pomodoroCycleCount == maxCycleCount) {
                    _timerStateFlow.value = TimerState.Finished
                } else {
                    if (_timerStateFlow.value is TimerState.Running) {
                        startBreak()
                    } else startCycle()
                }
            }
        }
        currentTimer?.start()
    }
}

sealed class TimerState {
    data object Idle : TimerState()
    data class Running(val remainingTime: Long) : TimerState()
    data class Break(val remainingTime: Long) : TimerState()
    data object Finished : TimerState()
}