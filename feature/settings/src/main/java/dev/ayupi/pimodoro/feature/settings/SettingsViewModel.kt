package dev.ayupi.pimodoro.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ayupi.pimodoro.core.data.repository.UserDataRepository
import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig
import dev.ayupi.pimodoro.core.model.data.DarkThemeConfig
import dev.ayupi.pimodoro.core.model.data.SoundData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    val settingsUiState: StateFlow<SettingsUiState> =
        userDataRepository.userData
            .map { userData ->
                SettingsUiState.Success(
                    settings = EditableSettings(
                        darkThemeConfig = userData.darkThemeConfig,
                        useDynamicColor = userData.useDynamicColor,
                        preferredBreakTime = userData.preferredBreakTime,
                        preferredPauseSound = userData.preferredPauseSound,
                        preferredResumeSound = userData.preferredResumeSound
                    ),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SettingsUiState.Loading
            )

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            userDataRepository.setDarkThemeConfig(darkThemeConfig)
        }
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean){
        viewModelScope.launch {
            userDataRepository.setDynamicColorPreference(useDynamicColor)
        }
    }

    fun updatePreferredBreakTime(breakTimeConfig: BreakTimeConfig) {
        viewModelScope.launch {
            userDataRepository.setBreakTimePreference(breakTimeConfig)
        }
    }
    fun updatePreferredPauseSound(soundData: SoundData) {
        viewModelScope.launch {
            userDataRepository.setPauseSound(soundData)
        }
    }
    fun updatePreferredResumeSound(soundData: SoundData) {
        viewModelScope.launch {
            userDataRepository.setResumeSound(soundData)
        }
    }
}

data class EditableSettings(
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val preferredBreakTime: BreakTimeConfig,
    val preferredResumeSound: SoundData,
    val preferredPauseSound: SoundData,
)

sealed interface SettingsUiState {
    data object Loading: SettingsUiState
    data class Success(val settings: EditableSettings) : SettingsUiState
}