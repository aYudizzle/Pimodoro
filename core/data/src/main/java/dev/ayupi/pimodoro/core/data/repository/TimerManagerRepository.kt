package dev.ayupi.pimodoro.core.data.repository

import dev.ayupi.pimodoro.core.manager.data.TimerState
import kotlinx.coroutines.flow.Flow

interface TimerManagerRepository {

    val timerData: Flow<TimerState>

    fun startPomodoro(maxCycleCount: Int)

    fun startBreak()

    fun stopTimer()

    fun resetTimer()
}