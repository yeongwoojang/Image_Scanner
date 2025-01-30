package com.example.imagescanner.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagescanner.domain.translate.TranslateManager
import com.example.imagescanner.presenter.state.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val translateManager: TranslateManager
): ViewModel() {
    private val TAG = "SplashViewModel"

    private var _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState = _splashState.asStateFlow()

    init {
        downloadModule()
    }

    private fun downloadModule() {
        viewModelScope.launch() {
            translateManager.downloadModule()
                .onSuccess {
                    _splashState.value = SplashState.Complete
                }
                .onFailure {
                    _splashState.value = SplashState.Error(it.message.orEmpty())
                }
            Log.d(TAG, "isDownloadModule: $splashState")
        }
    }
}