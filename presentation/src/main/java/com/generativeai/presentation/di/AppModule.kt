package com.generativeai.presentation.di


import com.generativeai.data.generativeModel.ToraGenerativeModel
import com.generativeai.domain.utils.Constants
import com.generativeai.presentation.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun exceptionHandler() : CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
        }
    }


    @Provides
    fun provideToraGenerativeModel() : ToraGenerativeModel{
        return ToraGenerativeModel(
            provideGeminiProModel(),
            provideGeminiProVisionModel()
        )
    }


    @Provides
    fun provideGeminiProVisionModel() : GenerativeModel{
        return GenerativeModel(
            modelName = Constants.GEMINI_PRO_VISION,
            apiKey = BuildConfig.gemini_api_Key
        )
    }

    @Provides
    fun provideGeminiProModel() : GenerativeModel{
        return GenerativeModel(
            modelName = Constants.GEMINI_PRO,
            apiKey = BuildConfig.gemini_api_Key
        )
    }

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher





