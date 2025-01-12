package com.example.imagescanner.presenter.viewmodel

import android.util.Log
import android.view.TextureView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagescanner.data.state.CameraState
import com.example.imagescanner.domain.camera.CameraManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraManager: CameraManager
): ViewModel() {
    private val _cameraState = MutableStateFlow<CameraState>(CameraState.Initializing)
    val cameraState = _cameraState.asStateFlow()

    private val ceh = CoroutineExceptionHandler { _, e ->
        _cameraState.value = CameraState.Error(e.message.orEmpty())
    }

    fun initializeCamera(cameraId: String, textureView: TextureView) {
        viewModelScope.launch(ceh) {
            _cameraState.value = CameraState.Initializing
           val camera = cameraManager.initializeCamera(textureView)
            _cameraState.value = CameraState.Ready(camera)
        }
    }

    fun release() {

    }
}