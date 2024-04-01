package com.generativeai.data.repositoryImpl

import com.generativeai.data.generativeModel.ToraGenerativeModel
import com.generativeai.domain.repository.ConversationalAiRepository
import com.generativeai.domain.repository.ImageTextAiRepository
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class ImageTextAiRepositoryImpl @Inject constructor(
    private var toraGenerativeModel: ToraGenerativeModel
): ImageTextAiRepository {

    override fun getImageAiTextModel(): GenerativeModel {
        return toraGenerativeModel.getImageTextAiModel()
    }
}