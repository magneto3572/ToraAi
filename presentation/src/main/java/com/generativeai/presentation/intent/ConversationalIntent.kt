package com.generativeai.presentation.intent

import com.generativeai.domain.model.PromptItem

sealed class ConversationalIntent {
    data class OnSendClick(val message: String) : ConversationalIntent()
    data class OnPromptClick(val prompt: PromptItem) : ConversationalIntent()
    data class OnValueChange(val query: String) : ConversationalIntent()
    data object Initial : ConversationalIntent()
    data object ClearAll : ConversationalIntent()
}
