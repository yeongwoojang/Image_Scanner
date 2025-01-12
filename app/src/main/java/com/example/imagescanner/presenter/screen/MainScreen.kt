package com.example.imagescanner.presenter.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MainScreen(
    moveToCamera: () -> Unit
) {
    Button(
        onClick = {
            //_ Open Camera
            moveToCamera()
        }
    ) {
        Text(text = "Open Camera")
    }
}