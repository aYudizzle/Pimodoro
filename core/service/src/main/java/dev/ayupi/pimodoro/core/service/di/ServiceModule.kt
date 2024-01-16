package dev.ayupi.pimodoro.core.service.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ayupi.pimodoro.core.data.repository.TimerManagerRepository
import dev.ayupi.pimodoro.core.service.TimerService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideTimerService() = TimerService()
}