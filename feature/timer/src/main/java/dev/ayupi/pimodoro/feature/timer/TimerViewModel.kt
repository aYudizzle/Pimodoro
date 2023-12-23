package dev.ayupi.pimodoro.feature.timer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ayupi.pimodoro.core.data.repository.UserDataRepository
import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    userDataRepository: UserDataRepository
) : ViewModel() {
    val intervalSetting = savedStateHandle.getStateFlow(key = INTERVAL_SETTING, initialValue = 1)

    val timerSetupUiState: StateFlow<TimerSetupUiState> =
        userDataRepository.userData.flatMapLatest { userData ->
            intervalSetting.map { interval ->
                val minutes = calculateMinutes(interval, userData.preferredBreakTime)
                TimerSetupUiState.Success(
                    minutes = minutes
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TimerSetupUiState.Loading
        )

    private fun calculateMinutes(interval: Int, breakTimeConfig: BreakTimeConfig): Int {
        if(interval == 1) {
            return 25
        }
        val pauseTime = when(breakTimeConfig) {
            BreakTimeConfig.QUARTER -> 15
            BreakTimeConfig.HALF -> 30
        }
        val pauseCount = interval - 1
        val countBigTimeouts = pauseCount / 4
        return 25 * interval + (pauseCount - countBigTimeouts) * 5 + (countBigTimeouts * pauseTime)
    }

    fun increaseInterval(interval: Int) {
        savedStateHandle[INTERVAL_SETTING] = interval + 1
    }

    fun decreaseInterval(interval: Int) {
        if(interval > 1) {
            savedStateHandle[INTERVAL_SETTING] = interval - 1
        }
    }

}

const val INTERVAL_SETTING = "intervalSetting"