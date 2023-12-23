package dev.ayupi.pimodoro.core.manager.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ayupi.pimodoro.core.datastore.PiPreferencesDataStore
import dev.ayupi.pimodoro.core.manager.data.SoundManager
import dev.ayupi.pimodoro.core.manager.data.SoundManagerImpl
import dev.ayupi.pimodoro.core.manager.data.TimerManager
import dev.ayupi.pimodoro.core.manager.data.TimerManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {

    @Provides
    @Singleton
    fun provideSoundManager(
        @ApplicationContext context: Context,
    ) : SoundManager = SoundManagerImpl(context)

    @Provides
    @Singleton
    fun provideTimerManager(piPreferencesDataStore: PiPreferencesDataStore) : TimerManager = TimerManagerImpl(piPreferencesDataStore = piPreferencesDataStore)
}