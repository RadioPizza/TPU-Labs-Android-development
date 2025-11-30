package ru.olegkravtsov.lab27

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var gravitySensor: Sensor? = null
    private lateinit var ballView: BallView
    private lateinit var infoText: TextView

    private val physicsHandler = Handler(Looper.getMainLooper())
    private var isPhysicsRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        ballView = findViewById(R.id.ballView)
        infoText = findViewById(R.id.infoText)

        gravitySensor?.let {
            infoText.text = getString(R.string.gravity_sensor_available, it.name)
        } ?: run {
            infoText.text = getString(R.string.gravity_sensor_not_available)
        }
    }

    override fun onResume() {
        super.onResume()
        gravitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
        startPhysicsLoop()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        stopPhysicsLoop()
    }

    private fun startPhysicsLoop() {
        isPhysicsRunning = true
        physicsHandler.post(physicsRunnable)
    }

    private fun stopPhysicsLoop() {
        isPhysicsRunning = false
        physicsHandler.removeCallbacks(physicsRunnable)
    }

    private val physicsRunnable = object : Runnable {
        override fun run() {
            if (isPhysicsRunning) {
                ballView.updatePhysics()
                updateInfoText()
                physicsHandler.postDelayed(this, 16)
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            ballView.setGravity(event.values[0], event.values[1])
        }
    }

    private fun updateInfoText() {
        val info = """
            ${getString(R.string.gravity_label)}: ${ballView.getGravityValues()}
            ${getString(R.string.position_label)}: ${ballView.getBallPosition()}
            ${getString(R.string.velocity_label)}: ${ballView.getVelocityValues()}
        """.trimIndent()
        infoText.text = info
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }
}