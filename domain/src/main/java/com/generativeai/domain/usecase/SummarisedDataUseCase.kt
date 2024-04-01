package com.generativeai.domain.usecase

import com.generativeai.domain.repository.SummarisedAiRepository
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SummarisedDataUseCase @Inject constructor(
    private val repository: SummarisedAiRepository
) {
    suspend operator fun invoke(inputText : String) :Flow<Result<GenerateContentResponse>> = repository.getSummariseModel().generateContentStream(inputText)
        .map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
}



