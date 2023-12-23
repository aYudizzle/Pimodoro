import dev.ayupi.feature.pomodoro.TimerState

/**
 * interface to represent the uiState
 * whether its loading or Success with appropriate
 * values.
 */
sealed interface PomodoroUiState {
    data object Loading : PomodoroUiState
    data class NotStarted(
        val timerState: TimerState
    ) : PomodoroUiState
    data class Finished(
        val timerState: TimerState
    ) : PomodoroUiState
    data class Success(
        val time: String,
        val timeMillis: Long,
        val pauseTime: String,
        val pauseMillis: Long,
        val timerState: TimerState,
        val intervalCycleCount: Int,
        val maxCycles: Int,
        val pauseTimeMaxValue: Long,
    ) : PomodoroUiState
}