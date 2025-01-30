package com.example.imagescanner.domain.exception

sealed class CameraError : Exception() {
    data class InitializationError(override val message: String) : CameraError()
    data class CaptureError(override val message: String) : CameraError()
    data class RecognitionError(override val message: String) : CameraError()
    data class TranslationError(override val message: String) : CameraError()
}