package com.example.myapp.desktop

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberDialogState
import com.example.myapp.app.common.di.appModule
import com.example.myapp.app.common.navigation.AppNavigation
import com.example.myapp.core.ui.theme.MyAppTheme
import com.example.myapp.libs.version.appVersion
import org.koin.core.context.startKoin
import java.awt.Desktop
import java.awt.Taskbar
import javax.imageio.ImageIO
import javax.swing.SwingUtilities

fun main() {
    System.setProperty("apple.awt.application.name", "MyApp")
    startKoin { modules(appModule) }
    application {
        var showAbout by remember { mutableStateOf(false) }

        val iconPainter = runCatching {
            val url = object {}.javaClass.getResource("/icon.png")
            url?.let { BitmapPainter(ImageIO.read(it).toComposeImageBitmap()) }
        }.getOrNull()

        runCatching {
            if (Taskbar.isTaskbarSupported()) {
                val taskbar = Taskbar.getTaskbar()
                if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                    val iconUrl = object {}.javaClass.getResource("/icon.png")
                    iconUrl?.let { taskbar.setIconImage(ImageIO.read(it)) }
                }
            }
        }

        runCatching {
            if (Desktop.isDesktopSupported()) {
                val desktop = Desktop.getDesktop()
                if (desktop.isSupported(Desktop.Action.APP_ABOUT)) {
                    desktop.setAboutHandler {
                        SwingUtilities.invokeLater { showAbout = true }
                    }
                }
            }
        }

        if (showAbout) {
            val isDark = isSystemInDarkTheme()
            val dialogState = rememberDialogState(width = 320.dp, height = 340.dp)
            DialogWindow(
                onCloseRequest = { showAbout = false },
                title = "About MyApp",
                state = dialogState,
            ) {
                MyAppTheme(darkTheme = isDark) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        AboutContent(
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

@Composable
private fun AboutContent(iconPainter: Painter?, appVersion: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (iconPainter != null) {
            Icon(
                painter = iconPainter,
                contentDescription = "App Icon",
                modifier = Modifier.size(64.dp),
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "MyApp",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Version $appVersion",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "A Kotlin Multiplatform application built with Compose Multiplatform.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "© 2026 My Company",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
