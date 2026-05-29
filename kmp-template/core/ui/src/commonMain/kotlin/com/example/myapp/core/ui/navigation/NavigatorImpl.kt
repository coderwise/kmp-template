package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.myapp.core.ui.util.pop
import com.example.myapp.core.ui.util.push
import org.koin.compose.koinInject

/**
 * Wraps [NavigationState] with a fixed [currentBackStack] captured at the call site.
 * This prevents cross-contamination between child navigators during root transitions,
 * where two screens can simultaneously compose with different [NavigationState.currentRootKey]s.
 */
private class ChildNavigator(
    private val delegate: NavigationState,
    override val currentBackStack: NavBackStack<NavKey>,
) : Navigator by delegate {

    override fun navigate(key: NavKey) {
        currentBackStack.push(key)
    }

    override fun navigateUp() {
        if (currentBackStack.size > 1) {
            currentBackStack.pop()
        } else {
            delegate.navigateUp()
        }
    }
}

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
    val navigationState = rememberNavigator(onEvent)
    // Capture the backstack key once at first composition. Re-deriving it from
    // currentRootKey on every recomposition would cause child navigators to share
    // the wrong backstack during animated root transitions.
    val backStack = remember(navigationState) {
        navigationState.getBackStack(navigationState.currentRootKey).also { stack ->
            if (stack.isEmpty()) stack.add(startDestination)
        }
    }
    return remember(navigationState) { ChildNavigator(navigationState, backStack) }
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
