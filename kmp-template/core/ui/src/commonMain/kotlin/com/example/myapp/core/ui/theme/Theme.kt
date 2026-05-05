package com.example.myapp.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MyAppTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalRadii provides Radii()
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) AppDarkColorScheme else AppLightColorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
