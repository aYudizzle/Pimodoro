package dev.ayupi.pimodoro.core.manager.model

import dev.ayupi.core.manager.R
import dev.ayupi.pimodoro.core.model.data.SoundData

enum class SoundDataEntity(val id: Int) {
    BELL1(id = R.raw.bell1),
    BELL2(id = R.raw.bell2),
    BELL3(id = R.raw.bell3),
    PIANO(id = R.raw.piano),
}

fun SoundDataEntity.toSoundData() =
    when (this) {
        SoundDataEntity.BELL1 -> SoundData.BELL1
        SoundDataEntity.BELL2 -> SoundData.BELL2
        SoundDataEntity.BELL3 -> SoundData.BELL3
        SoundDataEntity.PIANO -> SoundData.PIANO
    }

fun SoundData.toSoundDataEntity() =
    when(this) {
        SoundData.BELL1 -> SoundDataEntity.BELL1
        SoundData.BELL2 -> SoundDataEntity.BELL2
        SoundData.BELL3 -> SoundDataEntity.BELL3
        SoundData.PIANO -> SoundDataEntity.PIANO
    }
