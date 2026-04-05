package com.example.myapp.web

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.example.myapp.app.common.di.appModule
import com.example.myapp.app.common.di.webOverrideModule
import com.example.myapp.app.common.navigation.AppNavigation
import kotlinx.browser.document
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        allowOverride(true)
        modules(appModule, webOverrideModule)
    }
    ComposeViewport(document.body!!) { AppNavigation() }
}
