package dev.ayupi.pimodoro.core.manager.data

import dev.ayupi.pimodoro.core.model.data.SoundData

interface SoundManager {
    suspend fun playSound(soundData: SoundData)
}