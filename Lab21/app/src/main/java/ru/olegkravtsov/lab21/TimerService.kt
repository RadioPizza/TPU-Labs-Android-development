package ru.olegkravtsov.lab21

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {
    private val binder = TimerBinder()
    private var totalSeconds = 0
    private var remainingSeconds = 0
    private var isRunning = false
    private var timerJob: Job? = null
    private var updateCallback: ((Int) -> Unit)? = null

    private val notificationId = 1
    private val channelId = "timer_channel"

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    fun setUpdateCallback(callback: (Int) -> Unit) {
        updateCallback = callback
    }

    fun startTimer(minutes: Int, seconds: Int) {
        if (isRunning) return

        totalSeconds = minutes * 60 + seconds
        remainingSeconds = totalSeconds
        isRunning = true

        startForegroundService()
        startTimerCountdown()
    }

    fun stopTimer() {
        isRunning = false
        timerJob?.cancel()
        updateNotification(getString(R.string.timer_stopped))
        stopSelf()
    }

    fun getRemainingTime(): Int = remainingSeconds

    fun isTimerRunning(): Boolean = isRunning

    private fun startForegroundService() {
        createNotificationChannel()

        val notification = buildNotification(remainingSeconds, getString(R.string.timer_started))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(notificationId, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(notificationId, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.notification_timer),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Канал для уведомлений таймера"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(seconds: Int, status: String = getString(R.string.notification_timer)): Notification {
        val timeText = formatTime(seconds)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(status)
            .setContentText(String.format(getString(R.string.notification_remaining), timeText))
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(status: String = getString(R.string.notification_timer)) {
        val notification = buildNotification(remainingSeconds, status)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(notificationId, notification)
    }

    private fun startTimerCountdown() {
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (isRunning && remainingSeconds > 0) {
                delay(1000)
                remainingSeconds--

                updateNotification()
                updateCallback?.invoke(remainingSeconds)
            }

            if (remainingSeconds <= 0) {
                timerFinished()
            }
        }
    }

    private fun timerFinished() {
        isRunning = false
        updateNotification(getString(R.string.timer_finished_notification))
        updateCallback?.invoke(0)

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_REMOVE)
            } else {
                @Suppress("DEPRECATION")
                stopForeground(true)
            }
            stopSelf()
        }
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%d:%02d", min, sec)
    }

    override fun onDestroy() {
        super.onDestroy()
        timerJob?.cancel()
        isRunning = false
    }

    companion object {
        private const val FOREGROUND_SERVICE_TYPE_SPECIAL_USE = 1 shl 30
    }
}