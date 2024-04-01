package com.generativeai.presentation.event

sealed class ImageTextEvent {
    data class DummyEvent(val uri: String) : ImageTextEvent()
}
