package ru.olegkravtsov.lab5

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private var rows = 0
    private var cols = 0
    private var cellCount = 0

    // Храним текущий основной цвет
    private var currentColor = Color.RED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)

        rows = gridLayout.rowCount
        cols = gridLayout.columnCount
        cellCount = rows * cols

        // Создаем плитки и добавляем их в сетку
        createTiles()

        // Устанавливаем начальные цвета
        updateAllTiles()
    }

    override fun onResume() {
        super.onResume()
        // При возвращении в приложение меняем цвет
        generateNewColor()
        updateAllTiles()
    }

    private fun createTiles() {
        for (i in 0 until cellCount) {
            val textView = TextView(this).apply {
                // Делаем плитку видимой
                text = "" // Без текста
                textSize = 0f

                // Настраиваем размеры для заполнения ячейки
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(i / cols, 1f)
                    columnSpec = GridLayout.spec(i % cols, 1f)
                }
            }

            // При нажатии на любую плитку меняем все цвета
            textView.setOnClickListener {
                generateNewColor()
                updateAllTiles()
            }

            gridLayout.addView(textView)
        }
    }

    private fun generateNewColor() {
        // Простая генерация случайного цвета
        currentColor = Color.rgb(
            Random.nextInt(256), // Красный: 0-255
            Random.nextInt(256), // Зеленый: 0-255
            Random.nextInt(256)  // Синий: 0-255
        )
    }

    private fun updateAllTiles() {
        for (i in 0 until cellCount) {
            val textView = gridLayout.getChildAt(i) as TextView

            // Каждая плитка получает тот же цвет, но с разной прозрачностью
            // Первая плитка - самая прозрачная, последняя - самая насыщенная
            val transparency = 25 + (i * 15) // От 25 до 220

            val tileColor = Color.argb(
                transparency,
                Color.red(currentColor),
                Color.green(currentColor),
                Color.blue(currentColor)
            )

            textView.setBackgroundColor(tileColor)
        }
    }
}