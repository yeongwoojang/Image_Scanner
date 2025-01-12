package com.example.imagescanner.data.camera

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.view.Surface
import javax.inject.Inject

//_ 카메라 관련 리소스 관리 Manager
class CameraResourceManager @Inject constructor(){
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var surface: Surface? = null

    fun setCameraDevice(camera: CameraDevice) {
        cameraDevice = camera
    }

    fun setCaptureSession(session: CameraCaptureSession) {
        captureSession = session
    }

    fun setSurface(surface: Surface) {
        this.surface = surface
    }

    fun release() {
        try {
            captureSession?.close()
            captureSession = null

            cameraDevice?.close()
            cameraDevice = null

            surface?.release()
            surface = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}