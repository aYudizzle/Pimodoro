package dev.ayupi.pimodoro.core.datastore

import androidx.datastore.core.DataStore
import dev.ayupi.pimodoro.core.datastore.model.UserDataEntity
import dev.ayupi.pimodoro.core.datastore.model.toUserData
import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig
import dev.ayupi.pimodoro.core.model.data.DarkThemeConfig
import dev.ayupi.pimodoro.core.model.data.SoundData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PiPreferencesDataStore @Inject constructor(
    private val userPreferences: DataStore<UserDataEntity>
) {
    val userData = userPreferences.data
        .map {
            it.toUserData()
        }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData {
            it.copy(
                darkThemeConfig = darkThemeConfig
            )
        }
    }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.updateData {
            it.copy(
                useDynamicColor = useDynamicColor
            )
        }
    }

    suspend fun setBreakTimePreference(breakTimeConfig: BreakTimeConfig) {
        userPreferences.updateData {
            it.copy(
                preferredBreakTime = breakTimeConfig
            )
        }
    }

    suspend fun setPauseSound(soundData: SoundData) {
        userPreferences.updateData {
            it.copy(
                preferredPauseSound = soundData
            )
        }
    }

    suspend fun setResumeSound(soundData: SoundData) {
        userPreferences.updateData {
            it.copy(
                preferredResumeSound = soundData
            )
        }
    }
}