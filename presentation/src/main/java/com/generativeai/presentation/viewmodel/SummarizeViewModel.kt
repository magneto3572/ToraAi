package com.generativeai.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.generativeai.presentation.base.BaseViewModel
import com.generativeai.presentation.event.SummariseEvent
import com.generativeai.presentation.intent.SummariseIntent
import com.generativeai.presentation.uistate.SummarisedAiUiState
import com.generativeai.domain.model.PromptItem
import com.generativeai.domain.usecase.SummarisedDataUseCase
import com.generativeai.domain.utils.Constants
import com.generativeai.domain.utils.clear
import com.generativeai.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SummarizeViewModel @Inject constructor(
    private val summarisedDataUseCase: SummarisedDataUseCase,
    private val summarisedAiUiState: SummarisedAiUiState,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<SummarisedAiUiState, SummarisedAiUiState.PartialState, SummariseEvent, SummariseIntent>
    (savedStateHandle, summarisedAiUiState) {

    private val dataClassState: MutableStateFlow<SummarisedAiUiState> = MutableStateFlow(
        SummarisedAiUiState()
    )

    private var outputResponse = String()

    init {
        acceptIntent(SummariseIntent.Initial)
    }

    override fun mapIntents(intent: SummariseIntent): Flow<SummarisedAiUiState.PartialState> {
        return when (intent) {
            is SummariseIntent.OnPromptClick -> {
                setPromptItem(intent.prompt)
            }

            is SummariseIntent.Initial -> {
                setInitialPromptSuggestion()
            }

            is SummariseIntent.OnValueChange -> {
                setOnValueChange(intent.query)
            }

            SummariseIntent.ClearAll -> {
                return clearAllToDefault()
            }

            is SummariseIntent.OnSendClick -> {
                return getSummariseData(intent.message)
            }
        }
    }


    override fun reduceUiState(
        previousState: SummarisedAiUiState,
        partialState: SummarisedAiUiState.PartialState
    ): SummarisedAiUiState = when (partialState) {
        is SummarisedAiUiState.PartialState.Loading -> previousState.copy(
            isLoading = true,
            isError = false,
        )

        is SummarisedAiUiState.PartialState.Update -> previousState.copy(
            isLoading = false,
            data = partialState.generativeAiDataModel,
            isError = false,
        )

        is SummarisedAiUiState.PartialState.Error -> previousState.copy(
            isLoading = false,
            isError = true,
        )
    }

    private fun setInitialPromptSuggestion(): Flow<SummarisedAiUiState.PartialState> = flow {
        val promptSuggestionList = arrayListOf(
            PromptItem(
                heading = "Write a poem",
                subheading = "Write a poem about love",
                icon = R.drawable.write_prompt
            ),
            PromptItem(
                heading = "Translate",
                subheading = "Translate a sentence from one language to another",
                icon = R.drawable.write_prompt
            ),
            PromptItem(
                heading = "Alien",
                subheading = "Write a news headline about aliens landing on Earth.",
                icon = R.drawable.write_prompt
            ),
            PromptItem(
                heading = "Civilization",
                subheading = "Discovering a hidden civilization",
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
            SummarisedAiUiState.PartialState.Update(
                generativeAiDataModel = dataClassState.value.data
            )
        )
    }

    private fun setPromptItem(prompt: PromptItem): Flow<SummarisedAiUiState.PartialState> = flow {
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    query = prompt.subheading ?: "",
                )
            )
        }
        emit(
            SummarisedAiUiState.PartialState.Update(
                dataClassState.value.data
            )
        )
    }

    private fun setOnValueChange(query: String): Flow<SummarisedAiUiState.PartialState> = flow {
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    query = query,
                )
            )
        }
        emit(
            SummarisedAiUiState.PartialState.Update(
                generativeAiDataModel = dataClassState.value.data
            )
        )
    }


    private fun getSummariseData(prompt: String): Flow<SummarisedAiUiState.PartialState> = flow {
        summarisedDataUseCase(prompt).onStart {
            emit(SummarisedAiUiState.PartialState.Loading)
            sendUserPrompt(prompt)
            emit(SummarisedAiUiState.PartialState.Update(dataClassState.value.data))
            delay(500)
            sendInitialModelResponse()
            emit(SummarisedAiUiState.PartialState.Update(dataClassState.value.data))
        }.collect { result ->
            result
                .onSuccess { res ->
                    outputResponse.clear()
                    outputResponse = res.text.toString()
                    outputResponse.trimStart()
                    receiveModelGeneratedResponse(outputResponse)
                    emit(SummarisedAiUiState.PartialState.Update(dataClassState.value.data))
                }.onFailure {
                    emit(SummarisedAiUiState.PartialState.Error(it))
                }
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
                    aiModelType = summarisedAiUiState.data.aiModelType,
                )
            )
        }
    }

    private fun sendUserPrompt(prompt: String) {
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    query = it.data.query.clear(),
                    isCloseBtnVisible = false,
                    responseMessage = it.data.responseMessage.copy(
                        text = prompt.trimEnd(),
                        role = Constants.USER,
                        isGenerating = true
                    ),
                    aiModelType = summarisedAiUiState.data.aiModelType,
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

    private fun clearAllToDefault(): Flow<SummarisedAiUiState.PartialState> = flow {
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
            SummarisedAiUiState.PartialState.Update(
                dataClassState.value.data
            )
        )
    }
}