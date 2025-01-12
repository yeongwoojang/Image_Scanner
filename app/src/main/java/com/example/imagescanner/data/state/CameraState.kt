package com.example.imagescanner.data.state

import android.hardware.camera2.CameraDevice

sealed class CameraState {
    object Initializing : CameraState()
    data class Ready(val camera: CameraDevice) : CameraState()
    data class Error(val message: String) : CameraState()
}