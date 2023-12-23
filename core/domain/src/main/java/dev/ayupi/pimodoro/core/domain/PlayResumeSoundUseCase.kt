package dev.ayupi.pimodoro.core.domain

import dev.ayupi.pimodoro.core.data.repository.SoundManagerRepository
import dev.ayupi.pimodoro.core.data.repository.UserDataRepository
import dev.ayupi.pimodoro.core.model.data.SoundData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class PlayResumeSoundUseCase @Inject constructor(
    private val soundManagerRepository: SoundManagerRepository,
    private val userDataRepository: UserDataRepository
) {
    suspend fun execute() {
        val soundData = userDataRepository.userData.map {
            it.preferredResumeSound
        }
        soundData.collect {
            soundManagerRepository.playResumeSound(it)
        }
    }
}