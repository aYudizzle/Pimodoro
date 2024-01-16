package dev.ayupi.pimodoro.core.data.repository

import dev.ayupi.pimodoro.core.model.data.SoundData

interface SoundManagerRepository {
    suspend fun playResumeSound(soundData: SoundData)

    suspend fun playPauseSound(soundData: SoundData)
}