package com.generativeai.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.generativeai.presentation.base.BaseViewModel
import com.generativeai.domain.model.PromptItem
import com.generativeai.domain.usecase.ConversationalDataUseCase
import com.generativeai.domain.utils.Constants
import com.generativeai.domain.utils.clear
import com.generativeai.presentation.R
import com.generativeai.presentation.event.ConversationEvent
import com.generativeai.presentation.intent.ConversationalIntent
import com.generativeai.presentation.uistate.ConversationalAiUiState
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ConversationalAiViewModel @Inject constructor(
    private val conversationalDataUseCase: ConversationalDataUseCase,
    private val conversationalAiUiState: ConversationalAiUiState,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ConversationalAiUiState, ConversationalAiUiState.PartialState, ConversationEvent, ConversationalIntent>
    (savedStateHandle, conversationalAiUiState) {

    private val dataClassState: MutableStateFlow<ConversationalAiUiState> = MutableStateFlow(
        ConversationalAiUiState()
    )

    private var chatHistory: Array<Content> = arrayOf(
        content(role = "user") { text("Hello") },
        content(role = "model") { text("How may i assist you!") }
    )

    private var outputResponse = String()

    init {
        acceptIntent(ConversationalIntent.Initial)
    }

    override fun mapIntents(intent: ConversationalIntent): Flow<ConversationalAiUiState.PartialState> {
        return when (intent) {
            is ConversationalIntent.Initial -> {
                setInitialPromptSuggestion()
            }

            is ConversationalIntent.OnPromptClick -> {
                setPromptItem(intent.prompt.subheading ?: "")
            }

            is ConversationalIntent.OnValueChange -> {
                setOnValueChange(intent.query)
            }

            is ConversationalIntent.OnSendClick -> {
                getConversationalData(chatHistory.toList(), intent.message)
            }

            ConversationalIntent.ClearAll -> {
                clearAllToDefault()
            }
        }
    }


    override fun reduceUiState(
        previousState: ConversationalAiUiState,
        partialState: ConversationalAiUiState.PartialState
    ): ConversationalAiUiState = when (partialState) {
        is ConversationalAiUiState.PartialState.Loading -> previousState.copy(
            isLoading = true,
            isError = false,
        )

        is ConversationalAiUiState.PartialState.Update -> previousState.copy(
            isLoading = false,
            data = partialState.generativeAiDataModel,
            isError = false,
        )

        is ConversationalAiUiState.PartialState.Error -> previousState.copy(
            isLoading = false,
            isError = true,
        )
    }

    private fun setInitialPromptSuggestion(): Flow<ConversationalAiUiState.PartialState> = flow {
        val promptSuggestionList = arrayListOf(
            PromptItem(
                heading = "Business Email",
                subheading = "Write a business email requesting a meeting",
                icon = R.drawable.write_prompt
            ),
            PromptItem(
                heading = "Car Invention",
                subheading = "When was the first car invented",
                icon = R.drawable.write_prompt
            ),
            PromptItem(
                heading = "Advertising",
                subheading = "Create a social media post advertising a new product in tech",
                icon = R.drawable.write_prompt
            ),
            PromptItem(
                heading = "Historical Figure",
                subheading = "Write a story from the perspective of a historical figure",
                icon = R.drawable.write_prompt
            )
        )
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    promptSuggestionList = promptSuggestionList,
                    aiModelType = Constants.CONVERSATIONAL,
                )
            )
        }
        emit(
            ConversationalAiUiState.PartialState.Update(
                generativeAiDataModel = dataClassState.value.data
            )
        )
    }

    private fun getConversationalData(
        history: List<Content>,
        prompt: String
    ): Flow<ConversationalAiUiState.PartialState> = flow {
        conversationalDataUseCase(history, prompt).onStart {
            emit(ConversationalAiUiState.PartialState.Loading)
            sendUserPrompt(prompt)
            emit(ConversationalAiUiState.PartialState.Update(dataClassState.value.data))
            delay(500)
            sendInitialModelResponse()
            emit(ConversationalAiUiState.PartialState.Update(dataClassState.value.data))
        }.collect { result ->
            result
                .onSuccess { res ->
                    outputResponse.clear()
                    outputResponse = res.text.toString()
                    outputResponse.trimStart()
                    receiveModelGeneratedResponse(outputResponse)
                    chatHistory[1] = content(role = "model") { text(outputResponse) }
                    emit(ConversationalAiUiState.PartialState.Update(dataClassState.value.data))
                }.onFailure {
                    emit(ConversationalAiUiState.PartialState.Error(it))
                }
        }
    }

    private fun setPromptItem(prompt: String): Flow<ConversationalAiUiState.PartialState> = flow {
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    query = prompt,
                )
            )
        }
        emit(
            ConversationalAiUiState.PartialState.Update(
                dataClassState.value.data
            )
        )
    }

    private fun setOnValueChange(query: String): Flow<ConversationalAiUiState.PartialState> = flow {
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    query = query,
                )
            )
        }
        emit(
            ConversationalAiUiState.PartialState.Update(
                dataClassState.value.data
            )
        )
    }

    private fun sendUserPrompt(prompt: String) {
        chatHistory[0] = content(role = "user") { text(prompt) }
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    query = it.data.query.clear(),
                    responseMessage = it.data.responseMessage.copy(
                        text = prompt.trimEnd(),
                        role = Constants.USER,
                        isGenerating = true
                    ),
                    aiModelType = conversationalAiUiState.data.aiModelType,
                )
            )
        }
    }

    private fun receiveModelGeneratedResponse(outputResponse: String) {
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    responseMessage = it.data.responseMessage.copy(
                        text = outputResponse,
                        role = Constants.GEMINI,
                        isGenerating = false
                    ),
                    aiModelType = conversationalAiUiState.data.aiModelType,
                )
            )
        }
    }

    private fun sendInitialModelResponse() {
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    responseMessage = it.data.responseMessage.copy(
                        text = " ",
                        role = Constants.GEMINI,
                        isGenerating = true
                    ),
                )
            )
        }
    }

    private fun clearAllToDefault(): Flow<ConversationalAiUiState.PartialState> = flow {
        dataClassState.update {
            it.copy(
                isError = false,
                isLoading = false,
                data = it.data.copy(
                    responseMessage = it.data.responseMessage.copy(
                        text = it.data.responseMessage.text.clear(),
                        role = Constants.USER,
                        isGenerating = true
                    ),
                    aiModelType = it.data.aiModelType.clear(),
                )
            )
        }
        emit(
            ConversationalAiUiState.PartialState.Update(
                dataClassState.value.data
            )
        )
    }
}