package ru.olegkravtsov.lab6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var aEditText: EditText
    private lateinit var bEditText: EditText
    private lateinit var cEditText: EditText
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим все элементы
        aEditText = findViewById(R.id.aEditText)
        bEditText = findViewById(R.id.bEditText)
        cEditText = findViewById(R.id.cEditText)
        resultTextView = findViewById(R.id.resultTextView)

        // Добавляем обработчики изменений текста
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                calculateRoots()
            }
        }

        aEditText.addTextChangedListener(textWatcher)
        bEditText.addTextChangedListener(textWatcher)
        cEditText.addTextChangedListener(textWatcher)

        // Первоначальный расчет
        calculateRoots()
    }

    private fun calculateRoots() {
        val a = aEditText.text.toString().toDoubleOrNull()
        val b = bEditText.text.toString().toDoubleOrNull()
        val c = cEditText.text.toString().toDoubleOrNull()

        if (a == null || b == null || c == null) {
            resultTextView.text = "Введите все коэффициенты"
            return
        }

        if (a == 0.0) {
            resultTextView.text = "Коэффициент a = 0, уравнение линейное"
            return
        }

        // Вычисляем дискриминант
        val discriminant = b * b - 4 * a * c

        when {
            discriminant < 0 -> {
                resultTextView.text = "Действительных корней нет\n(D = $discriminant < 0)"
            }
            discriminant == 0.0 -> {
                val x = -b / (2 * a)
                resultTextView.text = "Найден один корень:\n$x\n(D = $discriminant)"
            }
            else -> {
                val x1 = (-b + sqrt(discriminant)) / (2 * a)
                val x2 = (-b - sqrt(discriminant)) / (2 * a)
                resultTextView.text = "Найдены два корня:\n$x1\n$x2\n(D = $discriminant)"
            }
        }
    }
}