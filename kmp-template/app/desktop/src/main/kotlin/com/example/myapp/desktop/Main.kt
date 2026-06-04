package com.example.myapp.desktop

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.myapp.app.common.di.appModule
import com.example.myapp.app.common.navigation.AppNavigation
import org.koin.core.context.startKoin
import java.awt.Taskbar
import javax.imageio.ImageIO

fun main() = application {
    startKoin { modules(appModule) }

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

    Window(
        onCloseRequest = ::exitApplication,
        title = "MyApp",
        icon = iconPainter,
    ) {
        AppNavigation()
    }
}
