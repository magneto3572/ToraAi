package com.generativeai.presentation.bottombar

import androidx.annotation.StringRes
import com.generativeai.presentation.screen.destinations.ConversationAiScreenRouteDestination
import com.generativeai.presentation.screen.destinations.ImageTextAiScreenRouteDestination
import com.generativeai.presentation.screen.destinations.SummarisedAiScreenRouteDestination
import com.generativeai.presentation.R
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: Int,
    @StringRes val label: Int
) {
    AskTora(ConversationAiScreenRouteDestination, com.generativeai.domain.R.drawable.ask_ai_icon, R.string.ask_tora),
    ImageAi(ImageTextAiScreenRouteDestination, R.drawable.image_ai_icon, R.string.image_ai),
    Summarised(SummarisedAiScreenRouteDestination, R.drawable.summarise_icon, R.string.summarised),
}