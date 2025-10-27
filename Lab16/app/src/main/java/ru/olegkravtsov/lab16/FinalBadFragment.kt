package ru.olegkravtsov.lab16

import androidx.navigation.fragment.findNavController

class FinalBadFragment : BaseStoryFragment() {
    override val imageRes = R.drawable.final_bad
    override val storyTextRes = R.string.final_bad_story

    override val choices = listOf(
        Choice(R.string.choice_retry) {
            findNavController().navigate(R.id.action_finalBadFragment_to_introFragment)
        }
    )
}