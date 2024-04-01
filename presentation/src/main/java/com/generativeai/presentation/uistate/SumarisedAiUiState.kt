package com.generativeai.presentation.uistate

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.generativeai.domain.model.GenerativeAiDataModel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Immutable
@Parcelize
data class SummarisedAiUiState(
    val isLoading: Boolean = false,
    val data: @RawValue GenerativeAiDataModel = GenerativeAiDataModel(),
    val isError: Boolean = false,
) : Parcelable {

    sealed class PartialState {
        data object Loading : PartialState()
        data class Update(val generativeAiDataModel: GenerativeAiDataModel) : PartialState()
        data class Error(val throwable: Throwable) : PartialState()
    }
}

