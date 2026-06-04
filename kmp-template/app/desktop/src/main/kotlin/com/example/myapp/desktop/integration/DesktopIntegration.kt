package com.example.myapp.desktop.integration

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.Desktop
import java.awt.Taskbar
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.SwingUtilities

private val iconUrl by lazy {
    runCatching { object {}.javaClass.getResource("/icon.png") }.getOrNull()
}

val appIcon: BufferedImage? by lazy {
    iconUrl?.let { runCatching { ImageIO.read(it) }.getOrNull() }
}

val iconPainter: Painter? by lazy {
    appIcon?.let { BitmapPainter(it.toComposeImageBitmap()) }
}

fun setupDockIcon() {
    val image = appIcon ?: return
    runCatching {
        if (Taskbar.isTaskbarSupported()) {
            val taskbar = Taskbar.getTaskbar()
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                taskbar.setIconImage(image)
            }
        }
    }
}

fun setupAboutHandler(onAbout: () -> Unit) {
    runCatching {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.APP_ABOUT)) {
                desktop.setAboutHandler { SwingUtilities.invokeLater(onAbout) }
            }
        }
    }
}
