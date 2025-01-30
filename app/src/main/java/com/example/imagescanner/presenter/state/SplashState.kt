package com.example.imagescanner.presenter.state

sealed class SplashState {
    object Loading : SplashState()
    object Complete : SplashState()
    data class Error(val message: String) : SplashState()
}