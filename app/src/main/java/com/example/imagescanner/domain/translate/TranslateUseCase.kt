package com.example.imagescanner.domain.translate

import android.util.Log
import com.example.imagescanner.domain.exception.CameraError
import javax.inject.Inject

class TranslateUseCase @Inject constructor(
    private val translateManager: TranslateManager
) {
    suspend operator fun invoke(text: String): Result<String> = runCatching {
        translateManager.translateText(text)
    }.onFailure { exception ->
        throw CameraError.TranslationError(exception.message.orEmpty())
    }
}