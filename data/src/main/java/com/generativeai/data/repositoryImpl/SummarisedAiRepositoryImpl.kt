package com.generativeai.data.repositoryImpl

import com.generativeai.data.generativeModel.ToraGenerativeModel
import com.generativeai.domain.repository.ConversationalAiRepository
import com.generativeai.domain.repository.SummarisedAiRepository
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class SummarisedAiRepositoryImpl @Inject constructor(
    private var toraGenerativeModel: ToraGenerativeModel
): SummarisedAiRepository {

    override fun getSummariseModel(): GenerativeModel {
        return  toraGenerativeModel.getSummarisedAiModel()
    }
}