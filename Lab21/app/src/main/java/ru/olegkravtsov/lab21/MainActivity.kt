package ru.olegkravtsov.lab21

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var minutesEditText: EditText
    private lateinit var secondsEditText: EditText
    private lateinit var startStopButton: Button
    private lateinit var timerStatusText: TextView

    private var timerService: TimerService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            timerService?.setUpdateCallback { remainingSeconds ->
                runOnUiThread {
                    updateTimerDisplay(remainingSeconds)
                }
            }
            isBound = true
            updateUIFromService()
        }

        override fun onServiceDisconnected(className: ComponentName) {
            isBound = false
            timerService = null
        }
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
        setupClickListeners()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }

        bindTimerService()
    }

    private fun initViews() {
        minutesEditText = findViewById(R.id.minutesEditText)
        secondsEditText = findViewById(R.id.secondsEditText)
        startStopButton = findViewById(R.id.startStopButton)
        timerStatusText = findViewById(R.id.timerStatusText)
    }

    private fun setupClickListeners() {
        startStopButton.setOnClickListener {
            if (timerService?.isTimerRunning() == true) {
                stopTimer()
            } else {
                startTimer()
            }
        }
    }

    private fun bindTimerService() {
        val intent = Intent(this, TimerService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun startTimer() {
        val minutes = minutesEditText.text.toString().toIntOrNull() ?: 0
        val seconds = secondsEditText.text.toString().toIntOrNull() ?: 0

        if (minutes == 0 && seconds == 0) {
            Toast.makeText(this, getString(R.string.set_timer_time), Toast.LENGTH_SHORT).show()
            return
        }

        if (seconds >= 60) {
            Toast.makeText(this, getString(R.string.seconds_max_error), Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, TimerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

        timerService?.startTimer(minutes, seconds)
        updateUIForRunningState()
    }

    private fun stopTimer() {
        timerService?.stopTimer()
        updateUIForStoppedState()
    }

    private fun updateUIFromService() {
        if (timerService?.isTimerRunning() == true) {
            val remainingSeconds = timerService?.getRemainingTime() ?: 0
            updateUIForRunningState()
            updateTimerDisplay(remainingSeconds)
        } else {
            updateUIForStoppedState()
        }
    }

    private fun updateUIForRunningState() {
        startStopButton.text = getString(R.string.stop_button)
        minutesEditText.isEnabled = false
        secondsEditText.isEnabled = false
    }

    private fun updateUIForStoppedState() {
        startStopButton.text = getString(R.string.start_button)
        minutesEditText.isEnabled = true
        secondsEditText.isEnabled = true
        timerStatusText.text = getString(R.string.timer_ready)
    }

    private fun updateTimerDisplay(remainingSeconds: Int) {
        if (remainingSeconds > 0) {
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            timerStatusText.text = String.format("%d:%02d", minutes, seconds)
        } else {
            updateUIForStoppedState()
            Toast.makeText(this, getString(R.string.timer_finished), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }
}