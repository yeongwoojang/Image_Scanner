package com.example.imagescanner.data.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.util.Log
import android.view.Surface
import android.view.TextureView
import com.example.imagescanner.data.exception.CameraException
import com.example.imagescanner.domain.camera.CameraUseCase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class CameraUseCaseImpl @Inject constructor(
    private val context: Context,
    private val cameraResourceManager: CameraResourceManager
): CameraUseCase{

    @SuppressLint("MissingPermission")
    override suspend fun initializeCamera(textureView: TextureView): CameraDevice =
        suspendCoroutine { continuation ->
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as android.hardware.camera2.CameraManager

            val cameraId: String = cameraManager.cameraIdList[0]
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(device: CameraDevice) {
                    cameraResourceManager.setCameraDevice(device)
                    continuation.resume(device)
                    createCameraPreviewSession(device, textureView)
                }

                override fun onDisconnected(device: CameraDevice) {
                    continuation.resumeWithException(
                        CameraException("Camera disconnected")
                    )
                    device.close()
                }

                override fun onError(device: CameraDevice, error: Int) {
                    continuation.resumeWithException(
                        CameraException("Camera error: $error")
                    )
                    device.close()
                }
            }, null)
        }


    override fun captureImage(textureView: TextureView): Bitmap? {
        return try {
            textureView.bitmap
        } catch (e: Exception) {
            null
        }
    }

    override fun release() {
        cameraResourceManager.release()
    }

    private fun createCameraPreviewSession(camera: CameraDevice, textureView: TextureView) {
        val texture = textureView.surfaceTexture
        texture?.setDefaultBufferSize(1920, 1080)

        val surface = Surface(texture)
        cameraResourceManager.setSurface(surface)

        camera.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    cameraResourceManager.setCaptureSession(session)
                    val previewRequest = camera.createCaptureRequest(
                        CameraDevice.TEMPLATE_PREVIEW
                    ).apply {
                        addTarget(surface)
                    }.build()

                    session.setRepeatingRequest(previewRequest, null, null)
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    // 에러 처리
                }
            },
            null
        )
    }
}