package com.example.myapp.app.common

import androidx.compose.ui.window.ComposeUIViewController
import com.example.myapp.app.common.di.appModule
import com.example.myapp.app.common.navigation.AppNavigation
import org.koin.core.context.startKoin
import org.koin.core.error.KoinApplicationAlreadyStartedException
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    try {
        startKoin { modules(appModule) }
    } catch (_: KoinApplicationAlreadyStartedException) {
        // already started — hot reload or re-entry
    }
    return ComposeUIViewController { AppNavigation() }
}
