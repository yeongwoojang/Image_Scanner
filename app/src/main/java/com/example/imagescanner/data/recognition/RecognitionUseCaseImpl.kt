package com.example.imagescanner.data.recognition

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import com.example.imagescanner.data.exception.RecognitionException
import com.example.imagescanner.domain.recognition.RecognitionUseCase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RecognitionUseCaseImpl @Inject constructor() : RecognitionUseCase {
    override suspend fun runTextRecognition(bitmap: Bitmap): Text =
        suspendCoroutine { continuation->
            try {
                //_ 1. 이미지 전처리
                val processedBitmap = preprocessBitmap(bitmap)

                //_ 2. 텍스트 인식 처리
                val image = InputImage.fromBitmap(processedBitmap, 0)
                val recognizer = TextRecognition.getClient(
                    KoreanTextRecognizerOptions.Builder()
                        .setExecutor(Executors.newSingleThreadExecutor())  //_ 별도 스레드에서 처리
                        .build()
                )

                recognizer.process(image)
                    .addOnSuccessListener { texts ->
                        continuation.resume(texts)
                    }
                    .addOnFailureListener { e ->
                        continuation.resumeWithException(
                            RecognitionException(e.message.orEmpty())
                        )
                        e.printStackTrace()
                    }
            } catch (e: Exception) {
                continuation.resumeWithException(
                    RecognitionException(e.message.orEmpty())
                )
            }
        }

    override fun processTextRecognitionResult(text: Text): String? {
        var result = ""

        //_ 텍스트 블록을 y좌표(상하)로 먼저 정렬하고, 같은 줄에 있는 텍스트는 x좌표(좌우)로 정렬
        val sortedBlocks = text.textBlocks.sortedWith(
            compareBy(
                { it.boundingBox?.top },  //_ 먼저 y좌표로 정렬
                { it.boundingBox?.left }  //_ 같은 y좌표 내에서 x좌표로 정렬
            )
        )

        //) 정렬된 블록 처리
        for (block in sortedBlocks) {
            //_ 라인 단위로 정렬
            val sortedLines = block.lines.sortedWith(
                compareBy(
                    { it.boundingBox?.top },
                    { it.boundingBox?.left }
                )
            )

            for (line in sortedLines) {
                //_ 요소 단위로 정렬
                val sortedElements = line.elements.sortedWith(
                    compareBy(
                        { it.boundingBox?.left }
                    )
                )

                //_ 정렬된 요소들의 텍스트 추가
                for (element in sortedElements) {
                    result += element.text + " "
                }
                result += "\n"  //_ 줄바꿈 추가
            }
            result += "\n"  //_ 블록 간 줄바꿈 추가
        }
        return result
    }

    private fun preprocessBitmap(bitmap: Bitmap): Bitmap {
        return try {
            //_ 1. 이미지 크기 조정 (너무 작지 않게)
            val scaledBitmap = Bitmap.createScaledBitmap(
                bitmap,
                bitmap.width * 2,  //_ 크기를 2배로
                bitmap.height * 2,
                true
            )

            //_ 2. 이미지 선명도 개선
            val matrix = ColorMatrix().apply {
                setSaturation(1.5f)  //_ 채도 증가
            }

            val paint = Paint().apply {
                colorFilter = ColorMatrixColorFilter(matrix)
            }

            val resultBitmap = Bitmap.createBitmap(
                scaledBitmap.width,
                scaledBitmap.height,
                Bitmap.Config.ARGB_8888
            )

            Canvas(resultBitmap).apply {
                drawBitmap(scaledBitmap, 0f, 0f, paint)
            }

            resultBitmap
        } catch (e: Exception) {
            bitmap  //_ 실패시 원본 반환
        }
    }
}