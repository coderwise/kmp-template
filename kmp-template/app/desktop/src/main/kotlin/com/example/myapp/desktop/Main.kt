package com.example.myapp.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.myapp.app.common.di.appModule
import com.example.myapp.app.common.navigation.AppNavigation
import org.koin.core.context.startKoin

fun main() = application {
    startKoin { modules(appModule) }
    Window(onCloseRequest = ::exitApplication, title = "MyApp") {
        AppNavigation()
    }
}
