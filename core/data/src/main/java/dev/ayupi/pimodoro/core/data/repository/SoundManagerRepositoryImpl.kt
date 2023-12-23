package dev.ayupi.pimodoro.core.data.repository

import dev.ayupi.pimodoro.core.manager.data.SoundManager
import dev.ayupi.pimodoro.core.model.data.SoundData
import javax.inject.Inject

class SoundManagerRepositoryImpl @Inject constructor(
    private val soundManager: SoundManager
) : SoundManagerRepository {
    override fun playResumeSound(soundData: SoundData) {
        soundManager.playSound(soundData)
    }

    override fun playPauseSound(soundData: SoundData) {
        soundManager.playSound(soundData)
    }
}