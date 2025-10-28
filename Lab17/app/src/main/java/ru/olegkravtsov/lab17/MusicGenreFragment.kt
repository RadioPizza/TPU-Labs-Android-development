package ru.olegkravtsov.lab17

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.olegkravtsov.lab17.databinding.FragmentMusicGenreBinding

class MusicGenreFragment : Fragment() {

    companion object {
        private const val ARG_GENRE = "genre"

        fun newInstance(genre: String): MusicGenreFragment {
            val args = Bundle().apply {
                putString(ARG_GENRE, genre)
            }
            return MusicGenreFragment().apply {
                arguments = args
            }
        }
    }

    private var _binding: FragmentMusicGenreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genre = arguments?.getString(ARG_GENRE) ?: getString(R.string.unknown_tab)
        binding.genreText.text = getString(R.string.music_genre_pattern, genre)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}