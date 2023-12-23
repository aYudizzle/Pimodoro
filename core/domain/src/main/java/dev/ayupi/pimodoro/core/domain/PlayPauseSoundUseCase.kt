package dev.ayupi.pimodoro.core.domain

import android.util.Log
import dev.ayupi.pimodoro.core.data.repository.SoundManagerRepository
import dev.ayupi.pimodoro.core.data.repository.UserDataRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayPauseSoundUseCase @Inject constructor(
    private val soundManagerRepository: SoundManagerRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend fun execute() {
        val soundData = userDataRepository.userData.map {
            it.preferredPauseSound
        }
        soundData.collect {
            soundManagerRepository.playPauseSound(it)
        }
    }
}