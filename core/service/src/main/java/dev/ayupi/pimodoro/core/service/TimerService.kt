package dev.ayupi.pimodoro.core.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.ayupi.pimodoro.core.data.repository.SoundManagerRepository
import dev.ayupi.pimodoro.core.data.repository.TimerManagerRepository
import dev.ayupi.pimodoro.core.manager.data.TimerState
import dev.ayupi.pimodoro.core.model.data.SoundData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : Service() {

    @Inject
    lateinit var timerManagerRepository: TimerManagerRepository

    @Inject
    lateinit var soundManagerRepository: SoundManagerRepository

    private lateinit var notificationManager: NotificationManagerCompat

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private val data: MutableStateFlow<TimerState> = MutableStateFlow(TimerState.Idle)

    private var timerDataJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this@TimerService)
        setForegroundService()
    }

    private fun startTimerDataCollection() {
        timerDataJob?.cancel() // Stopp alte Job, wenn vorhanden
        timerDataJob = scope.launch {
            timerManagerRepository.timerData.collect {
                val lastState = data.value
                data.value = it
                updateNotificication()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> setForegroundService()
            Actions.STOP.toString() -> stopTimerService()
        }
        return START_STICKY
    }

    private fun stopTimerService() {
        timerDataJob?.cancel()
        timerManagerRepository.stopTimer()
        stopSelf()
    }

    private fun setForegroundService() {
        val activityIntent = Intent(this@TimerService, javaClass)
        val activityPendingIntent = PendingIntent.getActivity(
            this@TimerService,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder =
            NotificationCompat.Builder(this@TimerService, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentIntent(activityPendingIntent)
                .setOngoing(true)
                .setContentTitle("Pimodoro Timer (Not Running)")
                .setContentText("No Timer started yet.")
                .setVibrate(null)

        val notification = notificationBuilder.build()
        startForeground(1, notification)
        startTimerDataCollection()
    }

    @SuppressLint("MissingPermission")
    private fun updateNotificication() {
        val activityIntent = Intent(this@TimerService, javaClass)
        val activityPendingIntent = PendingIntent.getActivity(
            this@TimerService,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder =
            NotificationCompat.Builder(this@TimerService, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentIntent(activityPendingIntent)
                .setOngoing(true)
                .setOnlyAlertOnce(true)

        when (data.value) {
            is TimerState.Running -> {
                val dataCast = data.value as TimerState.Running
                notificationBuilder
                    .setContentTitle("Pimodoro Timer (Running)")
                    .setContentText(dataCast.formattedTime)
            }

            is TimerState.Break -> {
                val dataCast = data.value as TimerState.Break
                notificationBuilder
                    .setContentTitle("Pimodoro Timer (Break)")
                    .setContentText(dataCast.formattedTime)
            }

            else -> {
                notificationBuilder
                    .setContentTitle("Pimodoro Timer (Not Running)")
                    .setContentText("No Timer started yet.")
            }
        }
        val notification = notificationBuilder.build()

        notificationManager.notify(1,notification)
    }
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "pimodoro_timer_channel"
    }
}

enum class Actions {
    START, STOP
}