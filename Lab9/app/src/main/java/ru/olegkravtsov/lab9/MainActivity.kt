package ru.olegkravtsov.lab9

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var countrySpinner: Spinner
    private lateinit var countryCodeTextView: TextView
    private lateinit var termsCheckBox: CheckBox
    private lateinit var registerButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Инициализация элементов
        initViews()

        // Настройка Spinner для выбора страны
        setupCountrySpinner()

        // Настройка CheckBox для согласия
        setupTermsCheckBox()

        // Настройка кнопок
        setupButtons()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        countrySpinner = findViewById(R.id.countrySpinner)
        countryCodeTextView = findViewById(R.id.countryCodeTextView)
        termsCheckBox = findViewById(R.id.termsCheckBox)
        registerButton = findViewById(R.id.registerButton)
        cancelButton = findViewById(R.id.cancelButton)

        // Установка начального кода страны
        countryCodeTextView.text = getString(R.string.default_country_code)
    }

    private fun setupCountrySpinner() {
        // Получаем массивы стран и кодов из ресурсов
        val countries = resources.getStringArray(R.array.countries)
        val countryCodes = resources.getStringArray(R.array.country_codes)

        // Устанавливаем адаптер для Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = adapter

        // Обработчик выбора страны
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                // Меняем код страны в соответствии с выбранной страной
                if (position in countryCodes.indices) {
                    countryCodeTextView.text = countryCodes[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Оставляем код по умолчанию
                countryCodeTextView.text = getString(R.string.default_country_code)
            }
        }
    }

    private fun setupTermsCheckBox() {
        // Устанавливаем начальное состояние кнопки в зависимости от CheckBox
        registerButton.isEnabled = termsCheckBox.isChecked

        // Обработчик изменения состояния CheckBox
        termsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            registerButton.isEnabled = isChecked
        }
    }

    private fun setupButtons() {
        // Обработчик для кнопки регистрации
        registerButton.setOnClickListener {
            // Здесь можно добавить логику регистрации
            Toast.makeText(this, "Регистрация выполнена!", Toast.LENGTH_SHORT).show()
        }

        // Обработчик для кнопки отмены - закрываем приложение
        cancelButton.setOnClickListener {
            finish()
        }
    }
}