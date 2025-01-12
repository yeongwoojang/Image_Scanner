package com.example.imagescanner.presenter.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.imagescanner.presenter.screen.CameraScreen
import com.example.imagescanner.presenter.screen.MainScreen

@Composable
fun AppNavigation(
    modifier: Modifier
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            MainScreen(moveToCamera = {
                navController.navigate(Screen.Camera.route)
            })
        }

        composable(Screen.Camera.route) {
            CameraScreen()
        }
    }
}