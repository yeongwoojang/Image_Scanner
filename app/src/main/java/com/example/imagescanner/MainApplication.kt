package com.example.imagescanner

import android.app.Application
import com.example.imagescanner.domain.translate.TranslateManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltAndroidApp
class MainApplication: Application() {

    @Inject
    lateinit var translateManager: TranslateManager
    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            //_ 다운로드 욀 때까지 로딩 필요?
            translateManager.downloadModule()
        }
    }
}