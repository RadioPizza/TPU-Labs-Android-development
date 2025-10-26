package ru.olegkravtsov.lab14

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.olegkravtsov.lab14.databinding.FragmentCityDetailBinding

class CityDetailFragment : Fragment() {

    private var _binding: FragmentCityDetailBinding? = null
    private val binding get() = _binding!!
    private var selectedCity: City? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем переданный город из аргументов
        arguments?.let {
            selectedCity = it.getParcelable("city")
        }

        // Если город не передан, пытаемся взять из Common
        if (selectedCity == null) {
            selectedCity = Common.selectedCity
        }

        binding.btnShowOnMap.setOnClickListener {
            selectedCity?.let { city ->
                showOnMap(city)
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
            binding.btnShowOnMap.isEnabled = true
        } ?: run {
            val unknown = getString(R.string.unknown)
            binding.tvCity.text = getString(R.string.city, unknown)
            binding.tvDistrict.text = getString(R.string.federal_district, unknown)
            binding.tvRegion.text = getString(R.string.region, unknown)
            binding.tvPostalCode.text = getString(R.string.postal_code, unknown)
            binding.tvTimezone.text = getString(R.string.timezone, unknown)
            binding.tvPopulation.text = getString(R.string.population, unknown)
            binding.tvFounded.text = getString(R.string.founded, unknown)
            binding.btnShowOnMap.isEnabled = false
        }
    }

    private fun showOnMap(city: City) {
        val geoUri = "geo:${city.lat},${city.lon}?z=12"
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))

        try {
            startActivity(mapIntent)
        } catch (e: Exception) {
            // Fallback
            val browserUri = "https://www.google.com/maps/@${city.lat},${city.lon},12z"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(browserUri))
            startActivity(browserIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(city: City): CityDetailFragment {
            val fragment = CityDetailFragment()
            val args = Bundle()
            args.putParcelable("city", city)
            fragment.arguments = args
            return fragment
        }
    }
}