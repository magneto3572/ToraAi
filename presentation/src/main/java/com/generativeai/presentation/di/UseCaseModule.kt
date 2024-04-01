package com.generativeai.presentation.di

import com.generativeai.domain.repository.ConversationalAiRepository
import com.generativeai.domain.repository.ImageTextAiRepository
import com.generativeai.domain.repository.SummarisedAiRepository
import com.generativeai.domain.usecase.ConversationalDataUseCase
import com.generativeai.domain.usecase.ImageTextDataUseCase
import com.generativeai.domain.usecase.SummarisedDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideSummarisedDataUseCase(
        repository: SummarisedAiRepository
    ): SummarisedDataUseCase {
        return SummarisedDataUseCase(repository)
    }

    @Provides
    fun provideConversationalDataUseCase(
        repository: ConversationalAiRepository,
    ): ConversationalDataUseCase {
        return ConversationalDataUseCase(repository)
    }

    @Provides
    fun provideImageTextDataUseCase(
        repository: ImageTextAiRepository,
    ): ImageTextDataUseCase {
        return ImageTextDataUseCase(repository)
    }
}
