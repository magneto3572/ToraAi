package com.generativeai.presentation.di



import com.generativeai.presentation.uistate.ConversationalAiUiState
import com.generativeai.presentation.uistate.ImageTextAiUiState
import com.generativeai.presentation.uistate.SummarisedAiUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal object UiStateModule {
    @Provides
    fun provideConversationalUiState(): ConversationalAiUiState = ConversationalAiUiState()

    @Provides
    fun provideSummarisedUiState(): SummarisedAiUiState = SummarisedAiUiState()

    @Provides
    fun provideImageTextUiState(): ImageTextAiUiState = ImageTextAiUiState()
}
