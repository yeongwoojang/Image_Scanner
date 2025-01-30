package com.example.imagescanner.domain.translate

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TranslateManager @Inject constructor() {
    private val TAG = "TranslateManager"
    private var isDownloaded = false

    val translator = Translation.getClient(
        TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.KOREAN)
            .build()
    )

    suspend fun downloadModule(): Result<Boolean> =
        suspendCancellableCoroutine<Result<Boolean>> { continuation ->
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()

            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    isDownloaded = true
                    Log.d(TAG, "downloadModelIfNeeded() | SUCCESS")
                    continuation.resume(Result.success(true))
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "downloadModelIfNeeded() | ERROR $exception")
                    continuation.resume(Result.failure(exception))
                }
        }

    suspend fun translateText(text: String): String {
        return suspendCancellableCoroutine<String> { continuation ->
            translator.translate(text)
                .addOnSuccessListener { translatedText ->
                    Log.d(TAG, "translateText() | text: $text | translatedText: $translatedText")
                    continuation.resume(translatedText)
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "translateText() | ERROR | $exception")
                    continuation.resumeWithException(exception)
                }
        }
    }
}