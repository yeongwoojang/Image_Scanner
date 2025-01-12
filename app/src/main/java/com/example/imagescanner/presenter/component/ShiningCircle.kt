package com.example.imagescanner.presenter.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun ShiningCircle(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    // 애니메이션 상태 관리
    val infiniteTransition = rememberInfiniteTransition(label = "shine")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shine"
    )

    // 그라데이션 회전 애니메이션
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .background(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = alpha),
                        Color.Gray.copy(alpha = alpha),
                        Color.White.copy(alpha = alpha)
                    ),
                    center = Offset(0.5f, 0.5f)
                )
            )
            .graphicsLayer {
                this.rotationZ = rotation
            }
    ) {
        Box(
            modifier = modifier.graphicsLayer {
                this.rotationZ = -rotation  // 회전 상쇄
            },
            contentAlignment = Alignment.Center
        ) {
            content()  // 텍스트 표시
        }
    }
}