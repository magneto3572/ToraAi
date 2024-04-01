package com.generativeai.presentation.intent

import com.generativeai.domain.model.PromptItem

sealed class SummariseIntent {
    data class OnValueChange(val query: String) : SummariseIntent()
    data class OnPromptClick(val prompt: PromptItem) : SummariseIntent()
    data object Initial : SummariseIntent()
    data class OnSendClick(val message: String) : SummariseIntent()
    data object ClearAll : SummariseIntent()
}
