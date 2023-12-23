package dev.ayupi.pimodoro.core.data.repository

import dev.ayupi.pimodoro.core.model.data.SoundData

interface SoundManagerRepository {
    fun playResumeSound(soundData: SoundData)

    fun playPauseSound(soundData: SoundData)
}