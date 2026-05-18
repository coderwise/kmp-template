package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation3.runtime.NavKey
import org.koin.compose.koinInject


@Composable
fun rememberAppNavigator(
    startRoot: NavKey,
    onEvent: Navigator.(Any) -> Unit = {}
): Navigator {
    val navigator = koinInject<NavigationState>()

    remember(navigator, startRoot) {
        navigator.onStart(startRoot)
    }

    val currentOnEvent by rememberUpdatedState(onEvent)
    LaunchedEffect(currentOnEvent) {
        navigator.setEventHandler(currentOnEvent)
    }

    return navigator
}
