package ru.olegkravtsov.lab10

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var messageText: EditText
    private lateinit var actionText: EditText
    private lateinit var backgroundR: SeekBar
    private lateinit var backgroundG: SeekBar
    private lateinit var backgroundB: SeekBar
    private lateinit var textR: SeekBar
    private lateinit var textG: SeekBar
    private lateinit var textB: SeekBar
    private lateinit var actionR: SeekBar
    private lateinit var actionG: SeekBar
    private lateinit var actionB: SeekBar
    private lateinit var showButton: Button

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
        setupButtonListener()
    }

    private fun initViews() {
        messageText = findViewById(R.id.messageText)
        actionText = findViewById(R.id.actionText)
        backgroundR = findViewById(R.id.backgroundR)
        backgroundG = findViewById(R.id.backgroundG)
        backgroundB = findViewById(R.id.backgroundB)
        textR = findViewById(R.id.textR)
        textG = findViewById(R.id.textG)
        textB = findViewById(R.id.textB)
        actionR = findViewById(R.id.actionR)
        actionG = findViewById(R.id.actionG)
        actionB = findViewById(R.id.actionB)
        showButton = findViewById(R.id.showButton)
    }

    private fun setupButtonListener() {
        showButton.setOnClickListener {
            showSnackbar()
        }
    }

    private fun showSnackbar() {
        val message = messageText.text.toString()
        val actionTextStr = actionText.text.toString()

        // Создаем Snackbar с сообщением
        val snackbar = Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG)

        // Устанавливаем цвет фона
        val backgroundColor = Color.rgb(
            backgroundR.progress,
            backgroundG.progress,
            backgroundB.progress
        )
        snackbar.setBackgroundTint(backgroundColor)

        // Устанавливаем цвет текста сообщения
        val textColor = Color.rgb(
            textR.progress,
            textG.progress,
            textB.progress
        )
        snackbar.setTextColor(textColor)

        // Если текст кнопки действия не пустой, добавляем действие
        if (actionTextStr.isNotEmpty()) {
            val actionColor = Color.rgb(
                actionR.progress,
                actionG.progress,
                actionB.progress
            )
            snackbar.setAction(actionTextStr) {
                // Действие при нажатии на кнопку
                Snackbar.make(findViewById(R.id.main), "Действие выполнено", Snackbar.LENGTH_SHORT).show()
            }
            snackbar.setActionTextColor(actionColor)
        }

        // Показываем Snackbar
        snackbar.show()
    }
}