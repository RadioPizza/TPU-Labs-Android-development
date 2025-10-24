package ru.olegkravtsov.lab13

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.olegkravtsov.lab13.databinding.ActivityCityListBinding

class CityListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val adapter = CityAdapter(Common.cities) { position ->
            val resultIntent = Intent().apply {
                putExtra("selected_city_index", position)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.rvCities.layoutManager = LinearLayoutManager(this)
        binding.rvCities.adapter = adapter
    }
}