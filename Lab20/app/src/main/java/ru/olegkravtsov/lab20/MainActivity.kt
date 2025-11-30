package ru.olegkravtsov.lab20

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var notificationButton: Button
    private lateinit var colorTextView: TextView
    private lateinit var notificationManager: NotificationManagerCompat

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showNotification()
        }
    }

    companion object {
        const val CHANNEL_ID = "lab20_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        createNotificationChannel()
        handleIntent(intent)
    }

    private fun initViews() {
        notificationButton = findViewById(R.id.notificationButton)
        colorTextView = findViewById(R.id.colorTextView)
        notificationManager = NotificationManagerCompat.from(this)

        notificationButton.setOnClickListener {
            checkNotificationPermission()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                showNotification()
            }
        } else {
            showNotification()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.channel_description)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        val remoteInput = RemoteInput.Builder(NotificationReceiver.KEY_TEXT_REPLY).run {
            setLabel(getString(R.string.remote_input_label))
            build()
        }

        val replyIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_SET_COLOR
        }

        val replyPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                this,
                0,
                replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                this,
                0,
                replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val setColorAction = NotificationCompat.Action.Builder(
            R.drawable.ic_launcher_foreground,
            getString(R.string.action_set_color),
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        val resetIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = NotificationReceiver.ACTION_RESET_COLOR
        }

        val resetPendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            resetIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val resetColorAction = NotificationCompat.Action.Builder(
            R.drawable.ic_launcher_foreground,
            getString(R.string.action_reset_color),
            resetPendingIntent
        ).build()

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            2,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_content))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(resetColorAction)
            .addAction(setColorAction)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun handleIntent(intent: Intent?) {
        when (intent?.getStringExtra("action")) {
            "reset" -> resetColor()
            "set_color" -> {
                val color = intent.getStringExtra("color")
                color?.let { setColor(it) }
            }
        }
    }

    private fun resetColor() {
        findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.mainLayout)
            .setBackgroundColor(Color.WHITE)
        colorTextView.visibility = android.view.View.GONE
    }

    private fun setColor(colorHex: String) {
        try {
            val color = Color.parseColor("#$colorHex")
            findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.mainLayout)
                .setBackgroundColor(color)

            colorTextView.text = getString(R.string.color_selected, colorHex)
            colorTextView.visibility = android.view.View.VISIBLE
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, getString(R.string.invalid_color_format), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
}