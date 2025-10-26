package ru.olegkravtsov.lab14

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.olegkravtsov.lab14.databinding.FragmentCityListBinding

class CityListFragment : Fragment() {

    private var _binding: FragmentCityListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CityAdapter
    private var layoutManagerState: android.os.Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Восстанавливаем состояние прокрутки
        if (savedInstanceState != null) {
            layoutManagerState = savedInstanceState.getParcelable("list_state")
        }

        adapter = CityAdapter(Common.cities) { city ->
            (requireActivity() as MainActivity).onCitySelected(city)
        }

        binding.rvCities.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCities.adapter = adapter

        // Восстанавливаем позицию прокрутки после создания разметки
        if (layoutManagerState != null) {
            binding.rvCities.layoutManager?.onRestoreInstanceState(layoutManagerState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем состояние прокрутки только если binding доступен
        _binding?.let { binding ->
            val state = binding.rvCities.layoutManager?.onSaveInstanceState()
            outState.putParcelable("list_state", state)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}