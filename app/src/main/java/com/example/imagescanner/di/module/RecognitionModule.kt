package com.example.imagescanner.di.module

import com.example.imagescanner.data.recognition.RecognitionUseCaseImpl
import com.example.imagescanner.domain.recognition.RecognitionUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RecognitionModule {

    @Binds
    abstract fun bindRecognitionUseCase(uc: RecognitionUseCaseImpl): RecognitionUseCase
}