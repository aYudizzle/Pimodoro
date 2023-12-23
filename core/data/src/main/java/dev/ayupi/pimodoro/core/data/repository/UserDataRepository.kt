package dev.ayupi.pimodoro.core.data.repository

import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig
import dev.ayupi.pimodoro.core.model.data.DarkThemeConfig
import dev.ayupi.pimodoro.core.model.data.SoundData
import dev.ayupi.pimodoro.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>

    /**
     * sets the user's prefered Theme
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    /**
     * if the user's device is capable of dynamic color - use preference
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    /**
     * sets the preference of the user's break configuration
     */
    suspend fun setBreakTimePreference(breakTimeConfig: BreakTimeConfig)

    /**
     * sets the preferred Resume SoundData
     */
    suspend fun setResumeSound(soundData: SoundData)

    /**
     * sets the preferred pause SoundData
     */
    suspend fun setPauseSound(soundData: SoundData)
}