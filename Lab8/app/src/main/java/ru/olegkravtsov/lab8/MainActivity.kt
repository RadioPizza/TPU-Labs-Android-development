package ru.olegkravtsov.lab8

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ConversionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[ConversionViewModel::class.java]

        val etMicrometers: EditText = findViewById(R.id.etMicrometers)
        val etMils: EditText = findViewById(R.id.etMils)
        val etMillimeters: EditText = findViewById(R.id.etMillimeters)
        val etLines: EditText = findViewById(R.id.etLines)
        val etInches: EditText = findViewById(R.id.etInches)

        // Наблюдаем за изменениями в микрометрах
        viewModel.micrometers.observe(this) { value ->
            if (etMicrometers.text.toString() != value) {
                etMicrometers.setText(value)
            }
        }

        // Наблюдаем за изменениями в милах
        viewModel.mils.observe(this) { value ->
            if (etMils.text.toString() != value) {
                etMils.setText(value)
            }
        }

        // Наблюдаем за изменениями в миллиметрах
        viewModel.millimeters.observe(this) { value ->
            if (etMillimeters.text.toString() != value) {
                etMillimeters.setText(value)
            }
        }

        // Наблюдаем за изменениями в линиях
        viewModel.lines.observe(this) { value ->
            if (etLines.text.toString() != value) {
                etLines.setText(value)
            }
        }

        // Наблюдаем за изменениями в дюймах
        viewModel.inches.observe(this) { value ->
            if (etInches.text.toString() != value) {
                etInches.setText(value)
            }
        }

        // Слушаем изменения в полях ввода
        etMicrometers.doAfterTextChanged {
            viewModel.updateMicrometers(it.toString())
        }

        etMils.doAfterTextChanged {
            viewModel.updateMils(it.toString())
        }

        etMillimeters.doAfterTextChanged {
            viewModel.updateMillimeters(it.toString())
        }

        etLines.doAfterTextChanged {
            viewModel.updateLines(it.toString())
        }

        etInches.doAfterTextChanged {
            viewModel.updateInches(it.toString())
        }
    }
}