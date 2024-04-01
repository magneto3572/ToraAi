package com.generativeai.data.repositoryImpl

import com.generativeai.data.generativeModel.ToraGenerativeModel
import com.generativeai.domain.repository.ConversationalAiRepository
import com.google.ai.client.generativeai.GenerativeModel
import javax.inject.Inject

class ConversationalAiRepositoryImpl @Inject constructor(
    private var toraGenerativeModel: ToraGenerativeModel
): ConversationalAiRepository {

    override fun getConversationalModel(): GenerativeModel {
        return toraGenerativeModel.getConversationalModel()
    }
}