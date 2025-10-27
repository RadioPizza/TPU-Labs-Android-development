package ru.olegkravtsov.lab16

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

abstract class BaseStoryFragment : Fragment() {

    abstract val imageRes: Int
    abstract val storyTextRes: Int
    abstract val choices: List<Choice>

    data class Choice(
        val textRes: Int,
        val action: () -> Unit
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.storyImage).setImageResource(imageRes)
        view.findViewById<TextView>(R.id.storyText).text = getString(storyTextRes)

        val buttonsContainer = view.findViewById<LinearLayout>(R.id.buttonsContainer)
        buttonsContainer.removeAllViews()

        choices.forEach { choice ->
            val button = Button(requireContext()).apply {
                text = getString(choice.textRes)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = resources.getDimensionPixelSize(R.dimen.button_bottom_margin)
                }

                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary_color))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                textSize = 16f
                setPadding(
                    resources.getDimensionPixelSize(R.dimen.button_padding),
                    resources.getDimensionPixelSize(R.dimen.button_padding),
                    resources.getDimensionPixelSize(R.dimen.button_padding),
                    resources.getDimensionPixelSize(R.dimen.button_padding)
                )

                setOnClickListener { choice.action() }
            }
            buttonsContainer.addView(button)
        }
    }
}