package com.example.myapp.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class WindowInfo(
    val isLandscape: Boolean = false,
    val screenWidth: Dp = 0.dp,
    val screenHeight: Dp = 0.dp
)

val LocalWindowInfo = compositionLocalOf { WindowInfo() }

val windowInfo: WindowInfo
    @Composable
    @ReadOnlyComposable
    get() = LocalWindowInfo.current
