package dev.ayupi.pimodoro.core.manager.data

import android.os.CountDownTimer
import android.util.Log
import dev.ayupi.pimodoro.core.datastore.PiPreferencesDataStore
import dev.ayupi.pimodoro.core.manager.model.TimerConfigEntity
import dev.ayupi.pimodoro.core.manager.model.toTimerConfigEntity
import dev.ayupi.pimodoro.core.manager.util.toFormattedTime
import dev.ayupi.pimodoro.core.model.data.SoundData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimerManagerImpl @Inject constructor(
    private val piPreferencesDataStore: PiPreferencesDataStore,
    private val soundManager: SoundManager,
) : TimerManager {
    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val pomodoroBeforeLongBreak = 4

    private val _timerStateFlow = MutableStateFlow<TimerState>(TimerState.Idle)
    override val timerStateFlow: Flow<TimerState> get() = _timerStateFlow.asStateFlow()
    private var currentTimer: CountDownTimer? = null
    private var pomodoroCycleCount: Int = 0
    private var maxCycleCount: Int = 0
    private var duration: Long = 0L
    private var breakTimeConfig: TimerConfigEntity = TimerConfigEntity.HALF
    private var soundDataPause: SoundData = SoundData.PIANO
    private var soundDataResume: SoundData = SoundData.PIANO


    override fun startPomodoro(maxCycleCount: Int) {
        scope.launch {
            piPreferencesDataStore.userData.collect {
                breakTimeConfig = it.preferredBreakTime.toTimerConfigEntity()
                soundDataPause = it.preferredPauseSound
                soundDataResume = it.preferredResumeSound
            }
        }
        this.maxCycleCount = maxCycleCount
        startCycle()
    }

    override fun resetTimer() {
        currentTimer?.cancel()
        pomodoroCycleCount = 0
        _timerStateFlow.update { TimerState.Idle }
    }

    override fun startBreak() {
        val breakDuration = if (pomodoroCycleCount % pomodoroBeforeLongBreak != 0) {
            TimerConfigEntity.SHORT.ms
        } else breakTimeConfig.ms

        startTimer(TimerState.Break(
            remainingTime = breakDuration,
            fullDuration = breakDuration,
            cycleCount = pomodoroCycleCount,
            formattedTime = breakDuration.toFormattedTime(),
            stateName = BREAK,
        ))
        scope.launch { soundManager.playSound(soundDataPause) }
    }

    fun startCycle() {
        pomodoroCycleCount++
        Log.i("Timer: ","Start von Cycle $pomodoroCycleCount")
        val cycleDuration = TimerConfigEntity.CYCLE.ms
        startTimer(TimerState.Running(
            remainingTime = cycleDuration,
            cycleCount = pomodoroCycleCount,
            formattedTime = cycleDuration.toFormattedTime(),
            stateName = RUNNING
        ))
        scope.launch { soundManager.playSound(soundDataResume) }
    }

    override fun stopTimer() {
        currentTimer?.cancel()
        _timerStateFlow.update { TimerState.Idle }
    }

    private fun startTimer(timerType: TimerState) {
        stopTimer() //if a timer is already running stop the timer before
        duration = when (timerType) {
            is TimerState.Running -> timerType.remainingTime //+ offset
            is TimerState.Break -> timerType.remainingTime
            else -> 0L
        }

        currentTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val updatedState = when(timerType) {
                    is TimerState.Break -> timerType.copy(remainingTime = millisUntilFinished, formattedTime = millisUntilFinished.toFormattedTime())
                    TimerState.Finished -> timerType
                    TimerState.Idle -> timerType
                    is TimerState.Running -> timerType.copy(remainingTime = millisUntilFinished, formattedTime = millisUntilFinished.toFormattedTime())
                }
                _timerStateFlow.update { updatedState }
            }

            override fun onFinish() {
                if (pomodoroCycleCount == maxCycleCount) {
                    _timerStateFlow.update { TimerState.Finished }
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
    data class Running(val remainingTime: Long, val fullDuration: Long = TimerConfigEntity.CYCLE.ms, val cycleCount: Int, val formattedTime: String, val stateName: String,) : TimerState()
    data class Break(val remainingTime: Long, val fullDuration: Long, val cycleCount: Int, val formattedTime: String, val stateName: String,) : TimerState()
    data object Finished : TimerState()
}

const val RUNNING = "Pomodoro"
const val BREAK = "Pause"