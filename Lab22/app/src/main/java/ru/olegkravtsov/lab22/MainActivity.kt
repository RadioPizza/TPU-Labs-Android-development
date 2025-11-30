package ru.olegkravtsov.lab22

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var tvStatus: TextView
    private lateinit var tvDistance: TextView
    private lateinit var btnNewPoint: Button
    private lateinit var btnSettings: Button

    private var targetLatitude: Double = 0.0
    private var targetLongitude: Double = 0.0
    private var isTargetSet: Boolean = false

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (isTargetSet) {
                updateDistance(location.latitude, location.longitude)
            }
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                startLocationUpdates()
                generateNewTargetPoint()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                startLocationUpdates()
                generateNewTargetPoint()
            }
            else -> {
                Toast.makeText(this, R.string.toast_permission_denied, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initViews()
        setupClickListeners()
        checkLocationPermissions()
    }

    private fun initViews() {
        tvStatus = findViewById(R.id.tvStatus)
        tvDistance = findViewById(R.id.tvDistance)
        btnNewPoint = findViewById(R.id.btnNewPoint)
        btnSettings = findViewById(R.id.btnSettings)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private fun setupClickListeners() {
        btnNewPoint.setOnClickListener {
            if (checkLocationPermissions()) {
                generateNewTargetPoint()
            }
        }

        btnSettings.setOnClickListener {
            openAppSettings()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startLocationUpdates()
                    true
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    Toast.makeText(
                        this,
                        R.string.toast_permission_rationale,
                        Toast.LENGTH_LONG
                    ).show()
                    requestLocationPermissions()
                    false
                }
                else -> {
                    requestLocationPermissions()
                    false
                }
            }
        } else {
            startLocationUpdates()
            true
        }
    }

    private fun requestLocationPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            10f,
            locationListener
        )
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000,
            10f,
            locationListener
        )
    }

    private fun generateNewTargetPoint() {
        try {
            val lastKnownLocation = getLastKnownLocation()
            if (lastKnownLocation != null) {
                val randomOffset = (Math.random() - 0.5) * 0.02
                targetLatitude = lastKnownLocation.latitude + randomOffset
                targetLongitude = lastKnownLocation.longitude + randomOffset
                isTargetSet = true

                tvStatus.text = getString(R.string.status_searching)
                tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark))
                updateDistance(lastKnownLocation.latitude, lastKnownLocation.longitude)

                Toast.makeText(this, R.string.toast_new_point, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.toast_location_error, Toast.LENGTH_LONG).show()
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, R.string.toast_location_access_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun getLastKnownLocation(): Location? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        var location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        return location
    }

    private fun updateDistance(currentLat: Double, currentLon: Double) {
        if (!isTargetSet) return

        val distance = calculateDistance(
            currentLat, currentLon,
            targetLatitude, targetLongitude
        )

        tvDistance.text = getString(R.string.distance_format, distance.toInt())

        if (distance <= 100) {
            tvStatus.text = getString(R.string.status_found)
            tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        } else {
            tvStatus.text = getString(R.string.status_searching)
            tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark))
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (checkLocationPermissions()) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
    }
}