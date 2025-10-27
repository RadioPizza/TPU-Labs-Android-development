package ru.olegkravtsov.lab16

import androidx.navigation.fragment.findNavController

class FinalGoodFragment : BaseStoryFragment() {
    override val imageRes = R.drawable.final_good
    override val storyTextRes = R.string.final_good_story

    override val choices = listOf(
        Choice(R.string.choice_restart) {
            findNavController().navigate(R.id.action_finalGoodFragment_to_introFragment)
        }
    )
}