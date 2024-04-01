package com.generativeai.presentation.di

import com.generativeai.data.repositoryImpl.ConversationalAiRepositoryImpl
import com.generativeai.data.repositoryImpl.ImageTextAiRepositoryImpl
import com.generativeai.data.repositoryImpl.SummarisedAiRepositoryImpl
import com.generativeai.domain.repository.ConversationalAiRepository
import com.generativeai.domain.repository.ImageTextAiRepository
import com.generativeai.domain.repository.SummarisedAiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {

    @Binds
    fun provideConversationalAiRepository(conversationalAiRepositoryImpl: ConversationalAiRepositoryImpl) : ConversationalAiRepository

    @Binds
    fun provideSummarisedAiRepository(summarisedAiRepositoryImpl: SummarisedAiRepositoryImpl) : SummarisedAiRepository

    @Binds
    fun provideImageTextAiRepository(imageTextAiRepositoryImpl: ImageTextAiRepositoryImpl) : ImageTextAiRepository
}