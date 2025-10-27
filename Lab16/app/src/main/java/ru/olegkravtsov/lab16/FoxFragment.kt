package ru.olegkravtsov.lab16

import androidx.navigation.fragment.findNavController

class FoxFragment : BaseStoryFragment() {
    override val imageRes = R.drawable.fox
    override val storyTextRes = R.string.fox_story

    override val choices = listOf(
        Choice(R.string.choice_obey) {
            findNavController().navigate(R.id.action_foxFragment_to_finalBadFragment)
        },
        Choice(R.string.choice_run) {
            findNavController().navigate(R.id.action_foxFragment_to_finalGoodFragment)
        }
    )
}