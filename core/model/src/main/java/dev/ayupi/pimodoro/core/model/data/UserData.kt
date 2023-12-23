package dev.ayupi.pimodoro.core.model.data

data class UserData(
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val preferredBreakTime: BreakTimeConfig,
    val preferredPauseSound: SoundData,
    val preferredResumeSound: SoundData
)
