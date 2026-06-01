package com.example.myapp.core.ui.theme

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.myapp.core.ui.util.LocalWindowInfo
import com.example.myapp.core.ui.util.WindowInfo

@Composable
fun MyAppTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    BoxWithConstraints {
        val windowInfo = WindowInfo(
            isLandscape = maxWidth > maxHeight,
            screenWidth = maxWidth,
            screenHeight = maxHeight
        )
        CompositionLocalProvider(
            LocalSpacing provides Spacing(),
            LocalRadii provides Radii(),
            LocalWindowInfo provides windowInfo
        ) {
            MaterialTheme(
                colorScheme = if (darkTheme) AppDarkColorScheme else AppLightColorScheme,
                typography = AppTypography,
                content = content
            )
        }
    }
}
