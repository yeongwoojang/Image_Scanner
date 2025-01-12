package com.example.imagescanner.presenter.viewmodel

import android.graphics.Bitmap
import android.util.Log
import android.view.TextureView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagescanner.data.state.CameraState
import com.example.imagescanner.domain.camera.CameraUseCase
import com.example.imagescanner.domain.recognition.RecognitionUseCase
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
    private val recognitionUseCase: RecognitionUseCase
): ViewModel() {
    private val _cameraState = MutableStateFlow<CameraState>(CameraState.Initializing)
    val cameraState = _cameraState.asStateFlow()

    private val ceh = CoroutineExceptionHandler { _, e ->
        _cameraState.value = CameraState.Error(e.message.orEmpty())
    }

    fun initializeCamera(textureView: TextureView) {
        viewModelScope.launch(ceh) {
            _cameraState.value = CameraState.Initializing
           val camera = cameraUseCase.initializeCamera(textureView)
            _cameraState.value = CameraState.Ready(camera)
        }
    }

    fun captureImage(textureView: TextureView) {
        viewModelScope.launch {
            val bitmap = cameraUseCase.captureImage(textureView)
            bitmap?.let {
                val text = recognitionUseCase.runTextRecognition(it)
                text?.let { text ->
                    val result = recognitionUseCase.processTextRecognitionResult(text)
                    Log.d("TEST_LOG", "result: $result")
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