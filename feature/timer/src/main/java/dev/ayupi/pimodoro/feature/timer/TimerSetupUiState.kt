package dev.ayupi.pimodoro.feature.timer

sealed interface TimerSetupUiState {
    data object Loading: TimerSetupUiState

    data class Success(
        val minutes: Int = 0
    ) : TimerSetupUiState
}