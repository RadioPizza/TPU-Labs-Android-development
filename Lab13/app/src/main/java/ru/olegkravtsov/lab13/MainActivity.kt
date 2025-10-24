package ru.olegkravtsov.lab13

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ru.olegkravtsov.lab13.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedCity: City? = null

    private val cityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val cityIndex = data?.getIntExtra("selected_city_index", -1) ?: -1
            if (cityIndex != -1 && cityIndex < Common.cities.size) {
                selectedCity = Common.cities[cityIndex]
                updateCityInfo()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Common.initCities(this)

        binding.btnChooseCity.setOnClickListener {
            val intent = Intent(this, CityListActivity::class.java)
            cityResultLauncher.launch(intent)
        }

        binding.btnShowOnMap.setOnClickListener {
            selectedCity?.let { city ->
                val geoUri = "geo:${city.lat},${city.lon}?z=12"
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))

                try {
                    startActivity(mapIntent)
                } catch (e: Exception) {
                    // Fallback
                    val browserUri = "https://www.google.com/maps/@${city.lat},${city.lon},12z"
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(browserUri)
                    )
                    startActivity(browserIntent)
                }
            }
        }

        updateCityInfo()
    }

    private fun updateCityInfo() {
        selectedCity?.let { city ->
            binding.tvCity.text = getString(R.string.city, city.title)
            binding.tvDistrict.text = getString(R.string.federal_district, city.district)
            binding.tvRegion.text = getString(R.string.region, city.region)
            binding.tvPostalCode.text = getString(R.string.postal_code, city.postalCode)
            binding.tvTimezone.text = getString(R.string.timezone, city.timezone)
            binding.tvPopulation.text = getString(R.string.population, city.population)
            binding.tvFounded.text = getString(R.string.founded, city.founded)
        } ?: run {
            val unknown = getString(R.string.unknown)
            binding.tvCity.text = getString(R.string.city, unknown)
            binding.tvDistrict.text = getString(R.string.federal_district, unknown)
            binding.tvRegion.text = getString(R.string.region, unknown)
            binding.tvPostalCode.text = getString(R.string.postal_code, unknown)
            binding.tvTimezone.text = getString(R.string.timezone, unknown)
            binding.tvPopulation.text = getString(R.string.population, unknown)
            binding.tvFounded.text = getString(R.string.founded, unknown)
        }
    }
}