package com.generativeai.domain.repository

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.Flow


interface ImageTextAiRepository {
    fun getImageAiTextModel() : GenerativeModel
}