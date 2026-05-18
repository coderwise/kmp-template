package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapp.core.ui.util.pop
import com.example.myapp.core.ui.util.push

@Stable
class NavigatorImpl(
    val backStack: NavBackStack<NavKey>,
    private val eventHandler: NavigatorImpl.(Any) -> Unit
) : Navigator {
    override val currentBackStack: NavBackStack<NavKey> get() = backStack
    fun push(destination: NavKey) = backStack.push(destination)
    override fun navigate(key: NavKey) = push(key)
    override fun switchRoot(key: NavKey) { /* Not supported for single stack */ }
    override fun onEvent(event: Any) = eventHandler(event)
    override fun navigateUp() = onEvent(GlobalNavEvent.Back)
    
    fun pop() { if (backStack.size > 1) backStack.pop() }
}

@Composable
fun rememberNavigator(
    startDestination: NavKey,
    configuration: SavedStateConfiguration,
    onEvent: NavigatorImpl.(Any) -> Unit = {}
): NavigatorImpl {
    val backStack = rememberNavBackStack(configuration, startDestination)
    val currentOnEvent by rememberUpdatedState(onEvent)
    return remember(backStack) { 
        NavigatorImpl(backStack) { event ->
            currentOnEvent(event)
        }
    }
}

@Stable
class NavigationStateNavigator(
    val state: NavigationState,
    private val eventHandler: NavigationStateNavigator.(Any) -> Unit
) : Navigator {
    override val currentBackStack: NavBackStack<NavKey>
        get() = state.currentBackStack

    override fun navigate(key: NavKey) {
        state.push(key)
    }

    override fun switchRoot(key: NavKey) {
        state.switchRoot(key)
    }

    override fun onEvent(event: Any) {
        eventHandler(event)
    }

    override fun navigateUp() = onEvent(GlobalNavEvent.Back)
    
    fun pop() {
        state.pop()
    }
}

@Composable
fun rememberAppNavigator(
    startRoot: NavKey,
    onEvent: NavigationStateNavigator.(Any) -> Unit = {}
): NavigationStateNavigator {
    val state = rememberNavigationState(startRoot)
    val currentOnEvent by rememberUpdatedState(onEvent)
    
    return remember(state) {
        NavigationStateNavigator(state) { event ->
            currentOnEvent(event)
        }
    }
}
