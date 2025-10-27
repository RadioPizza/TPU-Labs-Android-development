package ru.olegkravtsov.lab16

import androidx.navigation.fragment.findNavController

class WolfFragment : BaseStoryFragment() {
    override val imageRes = R.drawable.wolf
    override val storyTextRes = R.string.wolf_story

    override val choices = listOf(
        Choice(R.string.choice_run) {
            findNavController().navigate(R.id.action_wolfFragment_to_bearFragment)
        },
        Choice(R.string.choice_stay) {
            findNavController().navigate(R.id.action_wolfFragment_to_finalBadFragment)
        }
    )
}