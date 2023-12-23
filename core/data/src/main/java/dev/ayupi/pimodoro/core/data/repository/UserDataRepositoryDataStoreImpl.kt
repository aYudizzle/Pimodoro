package dev.ayupi.pimodoro.core.data.repository

import dev.ayupi.pimodoro.core.datastore.PiPreferencesDataStore
import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig
import dev.ayupi.pimodoro.core.model.data.DarkThemeConfig
import dev.ayupi.pimodoro.core.model.data.SoundData
import dev.ayupi.pimodoro.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataRepositoryDataStoreImpl @Inject constructor(
    private val piPreferencesDataStore: PiPreferencesDataStore,
) : UserDataRepository {
    override val userData: Flow<UserData> =
        piPreferencesDataStore.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        piPreferencesDataStore.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        piPreferencesDataStore.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setBreakTimePreference(breakTimeConfig: BreakTimeConfig) {
        piPreferencesDataStore.setBreakTimePreference(breakTimeConfig)
    }

    override suspend fun setResumeSound(soundData: SoundData) {
        piPreferencesDataStore.setResumeSound(soundData)
    }

    override suspend fun setPauseSound(soundData: SoundData) {
        piPreferencesDataStore.setPauseSound(soundData)
    }
}