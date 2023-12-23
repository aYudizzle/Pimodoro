package dev.ayupi.pimodoro.core.manager.data

import dev.ayupi.pimodoro.core.model.data.SoundData

interface SoundManager {
    fun playSound(soundData: SoundData)
}