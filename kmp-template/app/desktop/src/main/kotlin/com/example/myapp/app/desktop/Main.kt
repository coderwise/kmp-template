package com.example.myapp.app.desktop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.myapp.app.common.di.appModule
import com.example.myapp.app.common.navigation.AppNavigation
import com.example.myapp.app.desktop.integration.iconPainter
import com.example.myapp.app.desktop.integration.setupAboutHandler
import com.example.myapp.app.desktop.integration.setupDockIcon
import com.example.myapp.app.desktop.ui.components.AboutDialogContent
import com.example.myapp.libs.version.appVersion
import com.coderwise.libs.database.di.DATA_DIR_QUALIFIER
import com.coderwise.libs.settings.di.SETTINGS_DIR_QUALIFIER
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.File
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import com.example.myapp.core.ui.theme.MyAppTheme

fun main() {
    System.setProperty("apple.awt.application.name", "MyApp")
    startKoin {
        modules(
            appModule,
            // Where MyApp keeps its database and settings files on a desktop.
            module {
                single<File>(DATA_DIR_QUALIFIER) { File(System.getProperty("user.home"), ".myapp") }
                single<File>(SETTINGS_DIR_QUALIFIER) { File(System.getProperty("user.home"), ".myapp") }
            },
        )
    }
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
