package com.generativeai.domain.usecase

import android.util.Log
import com.generativeai.domain.repository.ConversationalAiRepository
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConversationalDataUseCase @Inject constructor(
    private val repository: ConversationalAiRepository
) {
    suspend operator fun invoke(
        history : List<Content>,
        prompt: String
    ): Flow<Result<GenerateContentResponse>> {
        Log.d("LogTagHistory", history.toString())
        return repository.getConversationalModel().startChat(history).sendMessageStream(prompt)
            .map {
                Result.success(it)
            }.catch {
            it.printStackTrace()
            emit(Result.failure(it))
        }
    }
}



