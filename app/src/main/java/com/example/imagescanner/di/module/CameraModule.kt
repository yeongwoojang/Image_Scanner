package com.example.imagescanner.di.module

import android.content.Context
import com.example.imagescanner.data.camera.CameraResourceManager
import com.example.imagescanner.data.camera.CameraUseCaseImpl
import com.example.imagescanner.domain.camera.CameraUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CameraModule {

    @Binds
    @Singleton
    abstract fun bindCameraModule(mg: CameraUseCaseImpl): CameraUseCase

    companion object {
        @Provides
        @Singleton
        fun provideCameraUseCaseImpl(
            @ApplicationContext context: Context,
            cameraResourceManager: CameraResourceManager
        ): CameraUseCaseImpl = CameraUseCaseImpl(context, cameraResourceManager)

        @Provides
        @Singleton
        fun provideCameraResourceManager(): CameraResourceManager = CameraResourceManager()
    }
}