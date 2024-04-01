package com.generativeai.data.generativeModel

import com.google.ai.client.generativeai.GenerativeModel
import javax.inject.Inject


class ToraGenerativeModel @Inject constructor(
    private var geminiProModel: GenerativeModel,
    private var geminiVisionProModel : GenerativeModel
) {

    fun getSummarisedAiModel() : GenerativeModel{
        return geminiProModel
    }

    fun getConversationalModel() : GenerativeModel{
        return geminiProModel
    }

    fun getImageTextAiModel()  : GenerativeModel{
        return geminiVisionProModel
    }
}