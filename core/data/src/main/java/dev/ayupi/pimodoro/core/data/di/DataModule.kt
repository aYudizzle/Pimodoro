package dev.ayupi.pimodoro.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ayupi.pimodoro.core.data.repository.SoundManagerRepository
import dev.ayupi.pimodoro.core.data.repository.SoundManagerRepositoryImpl
import dev.ayupi.pimodoro.core.data.repository.TimerManagerRepository
import dev.ayupi.pimodoro.core.data.repository.TimerManagerRepositoryImpl
import dev.ayupi.pimodoro.core.data.repository.UserDataRepository
import dev.ayupi.pimodoro.core.data.repository.UserDataRepositoryDataStoreImpl
import dev.ayupi.pimodoro.core.manager.data.SoundManager

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindUserDataRepository(
        userDataRepository: UserDataRepositoryDataStoreImpl
    ) : UserDataRepository

    @Binds
    fun bindSoundManagerRepository(
        soundManagerRepository: SoundManagerRepositoryImpl
    ) : SoundManagerRepository

    @Binds
    fun bindTimerManagerRepository(
        timerManagerRepository : TimerManagerRepositoryImpl
    ) : TimerManagerRepository
}