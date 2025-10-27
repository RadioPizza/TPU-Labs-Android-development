package ru.olegkravtsov.lab16

import androidx.navigation.fragment.findNavController

class HareFragment : BaseStoryFragment() {
    override val imageRes = R.drawable.hare
    override val storyTextRes = R.string.hare_story

    override val choices = listOf(
        Choice(R.string.choice_run) {
            findNavController().navigate(R.id.action_hareFragment_to_wolfFragment)
        },
        Choice(R.string.choice_stay) {
            findNavController().navigate(R.id.action_hareFragment_to_finalBadFragment)
        }
    )
}