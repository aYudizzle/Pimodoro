package dev.ayupi.pimodoro.core.manager.data

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.ayupi.pimodoro.core.manager.model.toSoundDataEntity
import dev.ayupi.pimodoro.core.model.data.SoundData

class SoundManagerImpl(private val context: Context) : SoundManager {
    override fun playSound(soundData: SoundData) {
        val soundEntity = soundData.toSoundDataEntity()
        MediaPlayer.create(context, soundEntity.id)?.start()
    }
}