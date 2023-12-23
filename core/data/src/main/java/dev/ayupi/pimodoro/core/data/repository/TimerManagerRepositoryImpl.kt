package dev.ayupi.pimodoro.core.data.repository

import dev.ayupi.pimodoro.core.manager.data.TimerManager
import javax.inject.Inject

class TimerManagerRepositoryImpl @Inject constructor(
    private val timerManager: TimerManager
) : TimerManagerRepository {
    override val timerData = timerManager.timerStateFlow

    override fun startPomodoro(maxCycleCount: Int) {
        timerManager.startPomodoro(maxCycleCount)
    }

    override fun startBreak() {
        timerManager.startBreak()
    }

    override fun stopTimer() {
        timerManager.stopTimer()
    }
}