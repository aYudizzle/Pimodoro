package dev.ayupi.pimodoro.core.datastore.model

import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig
import dev.ayupi.pimodoro.core.model.data.DarkThemeConfig
import dev.ayupi.pimodoro.core.model.data.SoundData
import dev.ayupi.pimodoro.core.model.data.UserData
import kotlinx.serialization.Serializable

@Serializable
data class UserDataEntity(
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val preferredBreakTime: BreakTimeConfig,
    val preferredPauseSound: SoundData,
    val preferredResumeSound: SoundData
)

fun UserDataEntity.toUserData(): UserData = UserData(
    darkThemeConfig = darkThemeConfig,
    useDynamicColor = useDynamicColor,
    preferredBreakTime = preferredBreakTime,
    preferredPauseSound = preferredPauseSound,
    preferredResumeSound = preferredResumeSound
)