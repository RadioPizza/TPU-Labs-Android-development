package ru.olegkravtsov.lab14

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.olegkravtsov.lab14.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isDualPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        Common.initCities(this)

        // Проверяем, есть ли контейнер для деталей (альбомная ориентация)
        isDualPane = findViewById<androidx.fragment.app.FragmentContainerView>(R.id.fragment_detail_container) != null

        // Всегда устанавливаем фрагменты заново при создании
        setupFragments()
    }

    private fun setupFragments() {
        // В портретной ориентации очищаем back stack при запуске
        if (!isDualPane) {
            supportFragmentManager.popBackStackImmediate(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        // Всегда показываем список городов в левом контейнере
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_list_container, CityListFragment())
            .commit()

        // Если альбомная ориентация, загружаем фрагмент деталей
        if (isDualPane) {
            val cityToShow = Common.selectedCity ?: Common.cities.firstOrNull()
            if (cityToShow != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_detail_container, CityDetailFragment.newInstance(cityToShow))
                    .commit()
            } else {
                // Если нет выбранного города, показываем пустой фрагмент деталей
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_detail_container, CityDetailFragment())
                    .commit()
            }
        }
    }

    fun onCitySelected(city: City) {
        // Сохраняем выбранный город
        Common.selectedCity = city

        if (isDualPane) {
            // Альбомная ориентация - заменяем фрагмент деталей
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_detail_container, CityDetailFragment.newInstance(city))
                .commit()
        } else {
            // Портретная ориентация - заменяем фрагмент списка на детали
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_list_container, CityDetailFragment.newInstance(city))
                .addToBackStack("city_detail")
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}