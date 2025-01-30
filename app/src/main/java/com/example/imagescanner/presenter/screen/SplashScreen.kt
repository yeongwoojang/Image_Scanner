package com.example.imagescanner.presenter.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.imagescanner.presenter.component.ShiningCircle
import com.example.imagescanner.presenter.state.SplashState
import com.example.imagescanner.presenter.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onCompleteSplash: () -> Unit
) {
    val TAG = "SplashScreen"
    val splashState = viewModel.splashState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(splashState.value) {
        when(splashState.value) {
            is SplashState.Complete -> onCompleteSplash()
            is SplashState.Error -> {
                Toast.makeText(context, "번역 모듈 다운로드 실패", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(

        ) {
            ShiningCircle(
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
            ) {

            }
        }
    }
}
