package dev.ayupi.pimodoro.core.manager.data

import kotlinx.coroutines.flow.Flow

interface TimerManager {
    val timerStateFlow: Flow<TimerState>

    fun startPomodoro(maxCycleCount: Int)

    fun startBreak()

    fun stopTimer()
}