package ru.olegkravtsov.lab17

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.olegkravtsov.lab17.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newsText.text = getString(R.string.news_section)
        binding.clearNewsButton.text = getString(R.string.clear_news)

        binding.clearNewsButton.setOnClickListener {
            (requireActivity() as MainActivity).clearNewsBadge()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setInNewsFragment(true)
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MainActivity).setInNewsFragment(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}