package com.example.imagescanner.domain.recognition

import android.graphics.Bitmap
import com.google.mlkit.vision.text.Text

interface RecognitionUseCase {
    suspend fun runTextRecognition(bitmap: Bitmap): Text?
    fun processTextRecognitionResult(text: Text): String?
}