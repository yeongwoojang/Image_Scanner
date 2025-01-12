package com.example.imagescanner.domain.camera

import android.graphics.Bitmap
import android.hardware.camera2.CameraDevice
import android.view.TextureView

interface CameraUseCase {
    suspend fun initializeCamera(textureView: TextureView): CameraDevice
    fun captureImage(textureView: TextureView): Bitmap?
    fun release()
}