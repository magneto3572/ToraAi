package com.generativeai.domain.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class GenerativeAiDataModel(
    val responseMessage : @RawValue Message = Message(),
    val promptSuggestionList : @RawValue ArrayList<PromptItem>? = null,
    val query : String = "",
    val aiModelType : String = "",
    val bitmaps : List<Bitmap> = emptyList(),
    val isCloseBtnVisible : Boolean = true,
    val isSearchWithImage : Boolean = false,
) : Parcelable