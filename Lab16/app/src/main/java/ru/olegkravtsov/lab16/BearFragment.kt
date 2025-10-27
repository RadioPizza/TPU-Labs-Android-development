package ru.olegkravtsov.lab16

import androidx.navigation.fragment.findNavController

class BearFragment : BaseStoryFragment() {
    override val imageRes = R.drawable.bear
    override val storyTextRes = R.string.bear_story

    override val choices = listOf(
        Choice(R.string.choice_run) {
            findNavController().navigate(R.id.action_bearFragment_to_foxFragment)
        },
        Choice(R.string.choice_stay) {
            findNavController().navigate(R.id.action_bearFragment_to_finalBadFragment)
        }
    )
}