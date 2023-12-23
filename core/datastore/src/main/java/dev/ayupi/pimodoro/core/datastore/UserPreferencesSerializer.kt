package dev.ayupi.pimodoro.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import dev.ayupi.pimodoro.core.datastore.model.UserDataEntity
import dev.ayupi.pimodoro.core.model.data.BreakTimeConfig
import dev.ayupi.pimodoro.core.model.data.DarkThemeConfig
import dev.ayupi.pimodoro.core.model.data.SoundData
import dev.ayupi.pimodoro.core.model.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserPreferencesSerializer: Serializer<UserDataEntity> {
    override val defaultValue = UserDataEntity(
        darkThemeConfig = DarkThemeConfig.SYSTEM_DEFAULT,
        preferredBreakTime = BreakTimeConfig.QUARTER,
        useDynamicColor = false,
        preferredResumeSound = SoundData.BELL3,
        preferredPauseSound = SoundData.BELL2,
    )

    override suspend fun readFrom(input: InputStream) =
        try {
            Json.decodeFromString(deserializer = UserDataEntity.serializer(), string = input.readBytes().decodeToString())
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: UserDataEntity, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(serializer = UserDataEntity.serializer(), value = t)
                    .encodeToByteArray()
            )
        }
    }
}