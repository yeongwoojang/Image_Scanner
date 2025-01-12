package com.example.imagescanner.presenter.component

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.view.Surface
import android.view.TextureView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.checkSelfPermission

@Composable
fun CameraPreview(
    modifier: Modifier,
    onTextureViewCreated: (TextureView) -> Unit
) {
    val context = LocalContext.current
    var textureView: TextureView? = null
    var camera: CameraDevice? by remember { mutableStateOf(null) }

    AndroidView(
        factory = { context ->
            TextureView(context).apply {
                surfaceTextureListener = object: TextureView.SurfaceTextureListener {
                    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                        //_ Surface 준비 완료
                    }

                    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                        //_ Surface 크기 변경 시
                    }

                    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                        //_ Surface Destroy 시
                        return false
                    }

                    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                        //_ Surface Update 시
                    }
                }
                textureView = this
                onTextureViewCreated(this)
            }
        },
        modifier = modifier
    )
}
