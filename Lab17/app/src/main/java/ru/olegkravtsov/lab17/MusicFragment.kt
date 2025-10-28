package ru.olegkravtsov.lab17

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.olegkravtsov.lab17.databinding.FragmentMusicBinding

class MusicFragment : Fragment() {

    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!

    private lateinit var genres: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Инициализируем список жанров в onCreate, где контекст уже доступен
        genres = listOf(
            getString(R.string.rock),
            getString(R.string.rap),
            getString(R.string.pop),
            getString(R.string.jazz),
            getString(R.string.classic),
            getString(R.string.edm),
            getString(R.string.techno),
            getString(R.string.house),
            getString(R.string.alternative),
            getString(R.string.folk)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MusicPagerAdapter(this)
        binding.pager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = genres[position]
        }.attach()
    }

    inner class MusicPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = genres.size

        override fun createFragment(position: Int): Fragment {
            return MusicGenreFragment.newInstance(genres[position])
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}