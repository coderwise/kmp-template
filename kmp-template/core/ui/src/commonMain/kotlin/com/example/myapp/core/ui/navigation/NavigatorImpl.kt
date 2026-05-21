package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation3.runtime.NavKey
import org.koin.compose.koinInject


@Composable
private fun rememberNavigator(onEvent: Navigator.(Any) -> Unit): NavigationState {
    val navigator = koinInject<NavigationState>()
    val currentOnEvent by rememberUpdatedState(onEvent)
    DisposableEffect(navigator, currentOnEvent) {
        navigator.addEventHandler(currentOnEvent)
        onDispose {
            navigator.removeEventHandler(currentOnEvent)
        }
    }
    return navigator
}

@Composable
fun rememberChildNavigator(
    startDestination: NavKey,
    onEvent: Navigator.(Any) -> Unit = {}
): Navigator {
    val navigator = rememberNavigator(onEvent)
    if (navigator.currentBackStack.isEmpty()) {
        navigator.currentBackStack.add(startDestination)
    }
    return navigator
}

@Composable
fun rememberAppNavigator(
    startRoot: NavKey,
    onEvent: Navigator.(Any) -> Unit = {}
): Navigator {
    val navigator = rememberNavigator(onEvent)
    remember(navigator, startRoot) {
        navigator.onStart(startRoot)
    }
    return navigator
}
