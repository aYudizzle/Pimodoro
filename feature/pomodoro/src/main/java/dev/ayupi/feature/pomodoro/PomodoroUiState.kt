import dev.ayupi.pimodoro.core.manager.data.TimerState

/**
 * interface to represent the uiState
 * whether its loading or Success with appropriate
 * values.
 */
sealed interface PomodoroUiState {
    data object Loading : PomodoroUiState
    data object NotStarted : PomodoroUiState
    data object Finished : PomodoroUiState
    data class Success(
        val remainingTime: Long,
        val fullDuration: Long,
        val formattedTime: String,
        val cycleCount: Int,
        val timerState: String,
        val maxCycleCount: Int,
    ) : PomodoroUiState
}