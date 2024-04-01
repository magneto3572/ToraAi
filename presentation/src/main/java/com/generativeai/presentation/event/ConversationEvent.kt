package com.generativeai.presentation.event

sealed class ConversationEvent {
    data class DummyEvent(val uri: String) : ConversationEvent()
}
