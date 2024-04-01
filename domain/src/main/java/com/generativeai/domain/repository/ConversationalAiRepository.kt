package com.generativeai.domain.repository

import com.google.ai.client.generativeai.GenerativeModel


interface ConversationalAiRepository {
    fun getConversationalModel() : GenerativeModel
}