package com.example.imagescanner.di.module

import android.content.Context
import com.example.imagescanner.data.camera.CameraManagerImpl
import com.example.imagescanner.domain.camera.CameraManager
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
    abstract fun bindCameraModule(mg: CameraManagerImpl): CameraManager

    companion object {
        @Provides
        @Singleton
        fun provideCameraManagerImpl(
            @ApplicationContext context: Context
        ): CameraManagerImpl {
            return CameraManagerImpl(context)
        }
    }
}