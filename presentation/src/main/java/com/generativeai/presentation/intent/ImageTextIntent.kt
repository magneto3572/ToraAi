package com.generativeai.presentation.intent

import android.graphics.Bitmap
import com.generativeai.domain.model.PromptItem

sealed class ImageTextIntent {
    data class OnValueChange(val query: String) : ImageTextIntent()
    data class OnPromptClick(val prompt: PromptItem) : ImageTextIntent()
    data class RemoveListItem(val index: Int) : ImageTextIntent()
    data class SendUriOrUrl(val uri: Any, val promptItem: PromptItem? = null) : ImageTextIntent()
    data class OnSendClick(val prompt: String, val selectedImages: List<Bitmap>) : ImageTextIntent()
    data object Initial : ImageTextIntent()
    data object ClearAll : ImageTextIntent()
}
