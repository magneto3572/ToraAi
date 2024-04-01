package com.generativeai.domain.usecase

import com.generativeai.domain.repository.ImageTextAiRepository
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ImageTextDataUseCase @Inject constructor(
    private val repository: ImageTextAiRepository
) {
    suspend operator fun invoke(
        inputText : Content
    ) :Flow<Result<GenerateContentResponse>> = repository.getImageAiTextModel().generateContentStream(inputText)
        .map {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
}



