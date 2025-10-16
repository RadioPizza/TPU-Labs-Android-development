package ru.olegkravtsov.lab7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var tvScore: TextView
    private lateinit var ivItem: ImageView
    private lateinit var tvQuestion: TextView
    private lateinit var btnEdible: Button
    private lateinit var btnInedible: Button

    // Данные для игры: пара (id изображения, является ли съедобным)
    private val items = listOf(
        // СЪЕДОБНОЕ (true)
        Pair(R.drawable.apple, true),          // Яблоко
        Pair(R.drawable.egg, true),            // Яйцо
        Pair(R.drawable.garlic, true),         // Чеснок
        Pair(R.drawable.ham, true),            // Ветчина
        Pair(R.drawable.ice_cream, true),      // Мороженое
        Pair(R.drawable.jam, true),            // Варенье
        Pair(R.drawable.octopus, true),        // Осьминог
        Pair(R.drawable.peach, true),          // Персик
        Pair(R.drawable.salmon, true),         // Лосось
        Pair(R.drawable.turkey, true),         // Индейка
        Pair(R.drawable.vanilla_flower, true), // Цветок ванили
        Pair(R.drawable.zucchini, true),       // Кабачок

        // НЕСЪЕДОБНОЕ (false)
        Pair(R.drawable.bag, false),           // Рюкзак
        Pair(R.drawable.ball, false),          // Мяч
        Pair(R.drawable.cactus, false),        // Кактус
        Pair(R.drawable.diamond, false),       // Алмаз
        Pair(R.drawable.fan, false),           // Вентилятор
        Pair(R.drawable.kettle, false),        // Чайник
        Pair(R.drawable.leaf, false),          // Лист
        Pair(R.drawable.moon, false),          // Луна
        Pair(R.drawable.nail, false),          // Гвоздь
        Pair(R.drawable.umbrella, false),      // Зонт
        Pair(R.drawable.yacht, false)          // Яхта
    )

    private var currentItemIndex = 0
    private var correctAnswers = 0
    private var totalAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupClickListeners()
        showRandomItem()
        updateScore()
    }

    private fun initViews() {
        tvScore = findViewById(R.id.tvScore)
        ivItem = findViewById(R.id.ivItem)
        tvQuestion = findViewById(R.id.tvQuestion)
        btnEdible = findViewById(R.id.btnEdible)
        btnInedible = findViewById(R.id.btnInedible)
    }

    private fun setupClickListeners() {
        btnEdible.setOnClickListener {
            checkAnswer(true)
        }

        btnInedible.setOnClickListener {
            checkAnswer(false)
        }
    }

    private fun showRandomItem() {
        try {
            currentItemIndex = Random.nextInt(items.size)
            val (imageRes, _) = items[currentItemIndex]
            ivItem.setImageResource(imageRes)
        } catch (e: Exception) {
            // Обработка ошибок загрузки изображений
            e.printStackTrace()
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val (_, isEdible) = items[currentItemIndex]
        totalAnswers++

        if (userAnswer == isEdible) {
            correctAnswers++
        }

        updateScore()
        showRandomItem()
    }

    private fun updateScore() {
        val scoreText = getString(R.string.score_format, correctAnswers, totalAnswers)
        tvScore.text = scoreText
    }
}