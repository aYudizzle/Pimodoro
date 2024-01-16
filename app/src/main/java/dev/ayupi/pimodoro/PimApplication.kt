package dev.ayupi.pimodoro

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import dagger.hilt.android.HiltAndroidApp
import dev.ayupi.pimodoro.core.service.TimerService

@HiltAndroidApp
class PimApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel =  NotificationChannel(
            TimerService.NOTIFICATION_CHANNEL_ID,
            "Pomodoro_Timer",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        channel.description = "Used for displaying the Pimodoro Timer"
        channel.enableVibration(false)
        channel.vibrationPattern = longArrayOf(0)
        channel.lockscreenVisibility = VISIBILITY_PUBLIC

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}