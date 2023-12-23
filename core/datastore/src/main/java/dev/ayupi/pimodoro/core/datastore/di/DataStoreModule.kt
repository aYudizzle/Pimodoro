package dev.ayupi.pimodoro.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ayupi.pimodoro.core.datastore.UserPreferencesSerializer
import dev.ayupi.pimodoro.core.datastore.model.UserDataEntity
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideUserDataStore(
        @ApplicationContext context: Context,
    ): DataStore<UserDataEntity> =
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer(),
            produceFile = { context.dataStoreFile("user_preferences.pb") }
        )
}