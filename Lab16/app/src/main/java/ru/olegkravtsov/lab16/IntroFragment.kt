package ru.olegkravtsov.lab16

import androidx.navigation.fragment.findNavController

class IntroFragment : BaseStoryFragment() {
    override val imageRes = R.drawable.elders
    override val storyTextRes = R.string.intro_story

    override val choices = listOf(
        Choice(R.string.choice_run) {
            findNavController().navigate(R.id.action_introFragment_to_hareFragment)
        },
        Choice(R.string.choice_stay) {
            findNavController().navigate(R.id.action_introFragment_to_finalBadFragment)
        }
    )
}