package ru.olegkravtsov.lab17

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var newsCount = 0
    private var timer: CountDownTimer? = null
    private var isInNewsFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNav = findViewById(R.id.bottom_navigation)

        // Загружаем первый фрагмент по умолчанию
        if (savedInstanceState == null) {
            replaceFragment(MusicFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_music -> {
                    replaceFragment(MusicFragment())
                    true
                }
                R.id.menu_books -> {
                    replaceFragment(BooksFragment())
                    true
                }
                R.id.menu_news -> {
                    isInNewsFragment = true
                    replaceFragment(NewsFragment())
                    true
                }
                else -> false
            }
        }

        startNewsTimer()
    }

    private fun startNewsTimer() {
        timer = object : CountDownTimer(1000000000, 2000) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isInNewsFragment) {
                    newsCount++
                    updateNewsBadge()
                }
            }

            override fun onFinish() {
                // Перезапускаем таймер
                startNewsTimer()
            }
        }.start()
    }

    private fun updateNewsBadge() {
        val badge = bottomNav.getOrCreateBadge(R.id.menu_news)
        badge.number = newsCount
        badge.isVisible = true
    }

    fun clearNewsBadge() {
        val badge = bottomNav.getOrCreateBadge(R.id.menu_news)
        badge.isVisible = false
        newsCount = 0
    }

    fun setInNewsFragment(value: Boolean) {
        isInNewsFragment = value
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment is NewsFragment) {
            isInNewsFragment = true
        } else {
            isInNewsFragment = false
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}