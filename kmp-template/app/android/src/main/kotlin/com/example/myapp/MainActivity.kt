package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.myapp.app.common.navigation.AppNavigation
import com.example.myapp.app.common.navigation.AppViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val appViewModel: AppViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Keep the splash up until the saved settings are known, so the first frame drawn is
        // already in the right theme instead of flashing the window's default one.
        installSplashScreen().setKeepOnScreenCondition { !appViewModel.isReady }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { AppNavigation() }
    }
}
