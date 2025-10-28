package ru.olegkravtsov.lab19

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnGetContacts: Button
    private lateinit var tvContacts: TextView

    // Лаунчер для запроса разрешения
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Разрешение получено - получаем контакты
            getAndDisplayContacts()
        } else {
            // Пользователь отказал - показываем объяснение
            showPermissionDeniedDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        btnGetContacts = findViewById(R.id.btnGetContacts)
        tvContacts = findViewById(R.id.tvContacts)
    }

    private fun setupClickListeners() {
        btnGetContacts.setOnClickListener {
            checkPermissionAndGetContacts()
        }
    }

    private fun checkPermissionAndGetContacts() {
        // Проверяем есть ли уже разрешение
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_CONTACTS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                // Разрешение уже есть - получаем контакты
                getAndDisplayContacts()
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS) -> {
                // Нужно показать объяснение пользователю
                showPermissionRationaleDialog()
            }

            else -> {
                // Запрашиваем разрешение без объяснения
                requestPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
            }
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_dialog_title))
            .setMessage(getString(R.string.permission_rationale_message))
            .setPositiveButton(getString(R.string.ok_button)) { _, _ ->
                requestPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
            }
            .setNegativeButton(getString(R.string.no_thanks_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.denied_dialog_title))
            .setMessage(getString(R.string.denied_dialog_message))
            .setPositiveButton(getString(R.string.open_settings_button)) { _, _ ->
                openAppSettings()
            }
            .setNegativeButton(getString(R.string.no_thanks_final_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        startActivity(intent)
    }

    @SuppressLint("Range")
    private fun getContacts(): List<String> {
        val result = mutableListOf<String>()

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )

        cursor?.use {
            val nameColumnIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            while (it.moveToNext()) {
                val name = it.getString(nameColumnIndex)
                if (!name.isNullOrEmpty()) {
                    result.add(name)
                }
            }
        }

        return result
    }

    private fun getAndDisplayContacts() {
        try {
            val contacts = getContacts()
            if (contacts.isNotEmpty()) {
                val contactsText = getString(R.string.success_message, contacts.joinToString("\n"))
                tvContacts.text = contactsText
            } else {
                tvContacts.text = getString(R.string.no_contacts_found)
            }
        } catch (e: SecurityException) {
            tvContacts.text = getString(R.string.permission_error)
        } catch (e: Exception) {
            tvContacts.text = getString(R.string.contacts_error, e.message ?: "неизвестная ошибка")
        }
    }
}