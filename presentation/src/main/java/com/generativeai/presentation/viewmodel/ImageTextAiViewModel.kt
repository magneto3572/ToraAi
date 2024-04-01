package com.generativeai.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import com.generativeai.presentation.base.BaseViewModel
import com.generativeai.presentation.event.ImageTextEvent
import com.generativeai.presentation.intent.ImageTextIntent
import com.generativeai.presentation.uistate.ImageTextAiUiState
import com.generativeai.domain.model.PromptItem
import com.generativeai.domain.usecase.ImageTextDataUseCase
import com.generativeai.domain.utils.Constants
import com.generativeai.domain.utils.clear
import com.generativeai.presentation.R
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ImageTextAiViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageTextDataUseCase: ImageTextDataUseCase,
    private val imageTextAiUiState: ImageTextAiUiState,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ImageTextAiUiState, ImageTextAiUiState.PartialState, ImageTextEvent, ImageTextIntent>
    (savedStateHandle, imageTextAiUiState) {

    private val dataClassState: MutableStateFlow<ImageTextAiUiState> = MutableStateFlow(
        ImageTextAiUiState()
    )

    private var bitmapList : ArrayList<Bitmap> = ArrayList()

    private var outputResponse = String()

    init {
        acceptIntent(ImageTextIntent.Initial)
    }

    override fun mapIntents(intent: ImageTextIntent): Flow<ImageTextAiUiState.PartialState> {
        when (intent) {
            is ImageTextIntent.Initial -> {
                return setInitialPromptSuggestion()
            }

            is ImageTextIntent.OnPromptClick -> {
                return setPromptItem(intent.prompt.subheading ?: "")
            }

            is ImageTextIntent.OnValueChange -> {
                return setOnValueChange(intent.query)
            }

            is ImageTextIntent.OnSendClick -> {
                return getImageTextData(intent.prompt, intent.selectedImages)
            }

            ImageTextIntent.ClearAll -> {
                return clearAllToDefault()
            }

            is ImageTextIntent.SendUriOrUrl -> {
                return getBitmapFromUrl(intent.uri, intent.promptItem, context = context)
            }

            is ImageTextIntent.RemoveListItem -> {
                Log.d("LogTagIndex2", intent.index.toString())
                return removeListItemState(intent.index)
            }
        }
    }

    private fun setPromptItem(prompt: String): Flow<ImageTextAiUiState.PartialState> = flow {
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    query = prompt,
                )
            )
        }
        emit(
            ImageTextAiUiState.PartialState.Update(
                dataClassState.value.data
            )
        )
    }


    override fun reduceUiState(
        previousState: ImageTextAiUiState,
        partialState: ImageTextAiUiState.PartialState
    ): ImageTextAiUiState = when (partialState) {
        is ImageTextAiUiState.PartialState.Loading -> previousState.copy(
            isLoading = true,
            isError = false,
        )

        is ImageTextAiUiState.PartialState.Update -> previousState.copy(
            isLoading = false,
            data = partialState.generativeAiDataModel,
            isError = false,
        )

        is ImageTextAiUiState.PartialState.Error -> previousState.copy(
            isLoading = false,
            isError = true,
        )
    }

    private fun setInitialPromptSuggestion(): Flow<ImageTextAiUiState.PartialState> = flow {
        val promptSuggestionList = arrayListOf(
            PromptItem(
                heading = "World of wonders",
                subheading = "Explain something about the wonder in image",
                url = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Eiffel_Tower_20051010.jpg/1595px-Eiffel_Tower_20051010.jpg?20080310200605",
                icon = R.drawable.write_prompt
            ),
            PromptItem(
                heading = "Fashion Assistant",
                subheading = "Suggest some sunglasses for my face",
                url = "https://media.istockphoto.com/id/1294339577/photo/young-beautiful-woman.jpg?s=612x612&w=0&k=20&c=v41m_jNzYXQtxrr8lZ9dE8hH3CGWh6HqpieWkdaMAAM=",
                icon = R.drawable.write_prompt
            ),
            PromptItem(
                heading = "Nature",
                subheading = "Analyze this image and provide a detailed description of the scene,",
                url = "https://www.deanmcleodphotography.com/images/960/True-Blue-1600px-DMP-Website.jpg",
                icon = R.drawable.write_prompt
            ),
            PromptItem(
                heading = "Technology",
                subheading = "Come up with a catchy advertising slogan based on the image",
                url = "https://images.indianexpress.com/2019/12/Macbook-Pro-2019-main-759.jpg?w=414",
                icon = R.drawable.write_prompt
            )
        )
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    promptSuggestionList = promptSuggestionList,
                    aiModelType = Constants.CONVERSATIONAL,
                    isSearchWithImage = true,
                )
            )
        }
        emit(
            ImageTextAiUiState.PartialState.Update(
                generativeAiDataModel = dataClassState.value.data
            )
        )
    }

    private fun setOnValueChange(query: String): Flow<ImageTextAiUiState.PartialState> = flow {
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    query = query,
                )
            )
        }
        emit(
            ImageTextAiUiState.PartialState.Update(
                generativeAiDataModel = dataClassState.value.data
            )
        )
    }

    private fun getImageTextData(
        prompt: String,
        selectedImages: List<Bitmap>
    ): Flow<ImageTextAiUiState.PartialState> = flow {
        val inputContent = content {
            for (bitmap in selectedImages) {
                image(bitmap)
            }
            text(prompt)
        }
        imageTextDataUseCase(inputContent).onStart {
            emit(ImageTextAiUiState.PartialState.Loading)
            sendUserPrompt(prompt)
            emit(ImageTextAiUiState.PartialState.Update(dataClassState.value.data))
            delay(500)
            sendInitialModelResponse()
            emit(ImageTextAiUiState.PartialState.Update(dataClassState.value.data))

        }.collect { result ->
            result
                .onSuccess { res ->
                    outputResponse.clear()
                    outputResponse = res.text.toString()
                    outputResponse.trimStart()
                    receiveModelGeneratedResponse(outputResponse)
                    emit(ImageTextAiUiState.PartialState.Update(dataClassState.value.data))
                }.onFailure {
                    emit(ImageTextAiUiState.PartialState.Error(it))
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
                    aiModelType = imageTextAiUiState.data.aiModelType,
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
                    aiModelType = imageTextAiUiState.data.aiModelType,
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

    private fun getBitmapFromUrl(
        url: Any,
        promptItem: PromptItem?,
        context: Context
    ): Flow<ImageTextAiUiState.PartialState> = flow {
        val imageRequest = ImageRequest.Builder(context)
            .data(url)
            .size(size = 768)
            .precision(Precision.EXACT)
            .build()
        runCatching {
            val result = ImageLoader.Builder(context).build().execute(imageRequest)
            if (result is SuccessResult) {
                bitmapList.clear()
                bitmapList.add((result.drawable as BitmapDrawable).bitmap)
                dataClassState.update {
                    it.copy(
                        data = it.data.copy(
                            query = promptItem?.subheading ?: it.data.responseMessage.text,
                            bitmaps = bitmapList.toList(),
                        )
                    )
                }
                emit(ImageTextAiUiState.PartialState.Update(dataClassState.value.data))
            }
        }.getOrElse { throwable ->
            throwable.printStackTrace()
        }
    }

    private fun clearAllToDefault(): Flow<ImageTextAiUiState.PartialState> = flow {
        bitmapList.clear()
        dataClassState.update {
            it.copy(
                isError = false,
                isLoading = false,
                data = it.data.copy(
                    isCloseBtnVisible = true,
                    query = it.data.query.clear(),
                    bitmaps = bitmapList.toList(),
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
            ImageTextAiUiState.PartialState.Update(
                dataClassState.value.data
            )
        )
    }

    private fun removeListItemState(index: Int): Flow<ImageTextAiUiState.PartialState> = flow {
        bitmapList.removeAt(index)
        dataClassState.update {
            it.copy(
                data = it.data.copy(
                    bitmaps = bitmapList.toList(),
                )
            )
        }
        emit(ImageTextAiUiState.PartialState.Update(dataClassState.value.data))
    }
}