package com.generativeai.domain.model

import com.generativeai.domain.utils.Constants

data class Message(
    val text: String = "",
    val role : String = Constants.USER,
    val isGenerating: Boolean? = true,
)

