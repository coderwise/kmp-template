package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapp.core.ui.util.pop
import com.example.myapp.core.ui.util.push

class NavigatorImpl(
    val backStack: NavBackStack<NavKey>,
    private val onEvent: NavigatorImpl.(Any) -> Unit
) : Navigator {
    fun push(destination: NavKey) = backStack.push(destination)
    fun pop() { if (backStack.size > 1) backStack.pop() }
    override fun navigate(event: Any) = onEvent(event)
    override fun navigateUp() = pop()
}

@Composable
fun rememberNavigator(
    startDestination: NavKey,
    configuration: SavedStateConfiguration,
    onEvent: NavigatorImpl.(Any) -> Unit = {}
): NavigatorImpl {
    val backStack = rememberNavBackStack(configuration, startDestination)
    return remember(backStack, onEvent) { NavigatorImpl(backStack, onEvent) }
}

class MultiStackNavigatorImpl(
    val stacks: MutableMap<NavKey, NavBackStack<NavKey>>,
    val currentKey: MutableState<NavKey>,
    val startKey: NavKey,
    private val onEvent: MultiStackNavigatorImpl.(Any) -> Unit
) : Navigator {
    val currentBackStack get() = stacks.getOrPut(currentKey.value) { NavBackStack(currentKey.value) }

    fun push(destination: NavKey) = currentBackStack.push(destination)

    fun pop() {
        if (currentBackStack.size > 1) {
            currentBackStack.pop()
        } else if (currentKey.value != startKey) {
            switchTab(startKey)
        }
    }

    fun switchTab(tab: NavKey) {
        stacks.getOrPut(tab) { NavBackStack(tab) }
        currentKey.value = tab
    }

    override fun navigate(event: Any) = onEvent(event)
    override fun navigateUp() = pop()
}

@Composable
fun rememberMultiStackNavigator(
    startKey: NavKey,
    onEvent: MultiStackNavigatorImpl.(Any) -> Unit = {}
): MultiStackNavigatorImpl {
    val stacks = remember { mutableStateMapOf(startKey to NavBackStack(startKey)) }
    val currentKey = remember { mutableStateOf(startKey) }
    return remember(stacks, currentKey, startKey, onEvent) {
        MultiStackNavigatorImpl(stacks, currentKey, startKey, onEvent)
    }
}
