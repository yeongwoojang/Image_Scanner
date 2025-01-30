package com.example.imagescanner.presenter.viewmodel

import android.graphics.Bitmap
import android.util.Log
import android.view.TextureView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagescanner.data.state.CameraState
import com.example.imagescanner.domain.camera.CameraUseCase
import com.example.imagescanner.domain.exception.CameraError
import com.example.imagescanner.domain.recognition.RecognitionUseCase
import com.example.imagescanner.domain.translate.TranslateUseCase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraUseCase: CameraUseCase,
    private val recognitionUseCase: RecognitionUseCase,
    private val translateUseCase: TranslateUseCase
): ViewModel() {
    private val _cameraState = MutableStateFlow<CameraState>(CameraState.Initializing)
    val cameraState = _cameraState.asStateFlow()

    private val ceh = CoroutineExceptionHandler { _, e ->
        _cameraState.value = CameraState.Error(e.message.orEmpty())
    }

    private val viewModelCeh = CoroutineExceptionHandler { _, e ->
        when (e) {
            is CameraError.InitializationError -> {
                _cameraState.value = CameraState.Error("카메라 초기화 실패: ${e.message}")
            }
            is CameraError.CaptureError -> {
                _cameraState.value = CameraState.Error("이미지 캡처 실패: ${e.message}")
            }
            is CameraError.RecognitionError -> {
                _cameraState.value = CameraState.Error("텍스트 인식 실패: ${e.message}")
            }
            is CameraError.TranslationError -> {
                _cameraState.value = CameraState.Error("번역 실패: ${e.message}")
            }
            else -> {
                _cameraState.value = CameraState.Error("알 수 없는 에러: ${e.message}")
            }
        }
    }

    fun initializeCamera(textureView: TextureView) {
        viewModelScope.launch(viewModelCeh) {
            _cameraState.value = CameraState.Initializing
           val camera = cameraUseCase.initializeCamera(textureView)
            _cameraState.value = CameraState.Ready(camera)
        }
    }

    fun captureImage(textureView: TextureView) {
        viewModelScope.launch(ceh) {
            val bitmap = cameraUseCase.captureImage(textureView)
            bitmap?.let {
                val text = recognitionUseCase.runTextRecognition(it)
                text?.let { text ->
                    val result = recognitionUseCase.processTextRecognitionResult(text)
                    val translatedText = translateUseCase(result?: "Hi").getOrNull()
                    Log.d("TEST_LOG", "translatedText: $translatedText")

                }?: {
                    Log.d("TEST_LOG", "result is null")
                }
            }
        }
    }

    fun release() {
        cameraUseCase.release()
    }
}