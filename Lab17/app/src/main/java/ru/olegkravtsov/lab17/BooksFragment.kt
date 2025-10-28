package ru.olegkravtsov.lab17

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import ru.olegkravtsov.lab17.databinding.FragmentBooksBinding

class BooksFragment : Fragment() {

    private var _binding: FragmentBooksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.new_books)))
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.read_books)))

        updateContent(0)

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateContent(tab?.position ?: 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun updateContent(position: Int) {
        val text = when (position) {
            0 -> getString(R.string.new_books_content)
            1 -> getString(R.string.read_books_content)
            else -> getString(R.string.unknown_tab)
        }
        binding.contentText.text = text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}