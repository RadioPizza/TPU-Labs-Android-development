package ru.olegkravtsov.lab4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var etFirst: EditText
    private lateinit var etSecond: EditText
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFirst = findViewById(R.id.etFirst)
        etSecond = findViewById(R.id.etSecond)
        tvResult = findViewById(R.id.tvResult)

        // Находим все кнопки
        val btAdd: Button = findViewById(R.id.btAdd)
        val btSubtract: Button = findViewById(R.id.btSubtract)
        val btMultiply: Button = findViewById(R.id.btMultiply)
        val btDivide: Button = findViewById(R.id.btDivide)

        // Создаем общий слушатель для всех кнопок
        val operationListener = View.OnClickListener { view ->
            val num1 = etFirst.text.toString().toDoubleOrNull()
            val num2 = etSecond.text.toString().toDoubleOrNull()

            if (num1 == null || num2 == null) {
                tvResult.text = "Ошибка: введите числа"
                return@OnClickListener
            }

            val result = when (view.id) {
                R.id.btAdd -> num1 + num2
                R.id.btSubtract -> num1 - num2
                R.id.btMultiply -> num1 * num2
                R.id.btDivide -> {
                    if (num2 != 0.0) {
                        num1 / num2
                    } else {
                        tvResult.text = "Ошибка: деление на ноль"
                        return@OnClickListener
                    }
                }
                else -> return@OnClickListener
            }

            // Форматируем вывод
            val operationSymbol = when (view.id) {
                R.id.btAdd -> "+"
                R.id.btSubtract -> "–"
                R.id.btMultiply -> "×"
                R.id.btDivide -> "/"
                else -> "?"
            }

            tvResult.text = "$num1 $operationSymbol $num2 = $result"
        }

        // Назначаем один и тот же слушатель на все кнопки
        btAdd.setOnClickListener(operationListener)
        btSubtract.setOnClickListener(operationListener)
        btMultiply.setOnClickListener(operationListener)
        btDivide.setOnClickListener(operationListener)
    }
}