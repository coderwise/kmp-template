package com.example.myapp.desktop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.myapp.app.common.di.appModule
import com.example.myapp.app.common.navigation.AppNavigation
import com.example.myapp.desktop.integration.iconPainter
import com.example.myapp.desktop.integration.setupAboutHandler
import com.example.myapp.desktop.integration.setupDockIcon
import com.example.myapp.desktop.ui.components.AboutDialogContent
import com.example.myapp.libs.version.appVersion
import org.koin.core.context.startKoin
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import com.example.myapp.core.ui.theme.MyAppTheme

fun main() {
    System.setProperty("apple.awt.application.name", "MyApp")
    startKoin { modules(appModule) }
    application {
        var showAbout by remember { mutableStateOf(false) }

        setupDockIcon()
        setupAboutHandler { showAbout = true }

        if (showAbout) {
            val isDark = isSystemInDarkTheme()
            DialogWindow(
                onCloseRequest = { showAbout = false },
                title = "About MyApp",
            ) {
                MyAppTheme(darkTheme = isDark) {
                    Surface {
                        AboutDialogContent(
                            iconPainter = iconPainter,
                            appVersion = appVersion,
                        )
                    }
                }
            }
        }

        Window(
            onCloseRequest = ::exitApplication,
            title = "MyApp",
            icon = iconPainter,
        ) {
            AppNavigation()
        }
    }
}
