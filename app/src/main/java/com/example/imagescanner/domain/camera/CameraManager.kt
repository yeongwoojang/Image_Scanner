package com.example.imagescanner.domain.camera

import android.hardware.camera2.CameraDevice
import android.view.TextureView

interface CameraManager {
    suspend fun initializeCamera(textureView: TextureView): CameraDevice
    fun release()
}