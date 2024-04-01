package com.generativeai.presentation.event

sealed class SummariseEvent {
    data class DummyEvent(val uri: String) : SummariseEvent()
}
