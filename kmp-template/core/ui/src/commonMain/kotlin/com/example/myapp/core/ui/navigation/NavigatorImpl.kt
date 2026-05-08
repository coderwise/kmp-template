package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapp.core.ui.util.pop
import com.example.myapp.core.ui.util.push

interface NavigatorScope {
    fun push(destination: NavKey)
    fun pop()
    fun switchTab(tab: NavKey) = Unit
}

// ---- Single stack ----

@Composable
fun rememberNavigationState(
    configuration: SavedStateConfiguration,
    startDestination: NavKey,
): NavigationState {
    val backStack = rememberNavBackStack(configuration, startDestination)

    return remember(startDestination) {
        NavigationState(backStack)
    }
}

class NavigationState(
    val backStack: NavBackStack<NavKey>
)

class NavigatorImpl internal constructor(
    val state: NavigationState,
    private val onEvent: NavigatorScope.(Any) -> Unit
) : Navigator, NavigatorScope {
    val backStack: NavBackStack<NavKey> get() = state.backStack

    override fun push(destination: NavKey) { state.backStack.push(destination) }
    override fun pop() { if (state.backStack.size > 1) state.backStack.pop() }

    override fun navigate(event: Any) { this.onEvent(event) }
    override fun navigateUp() { pop() }
}

@Composable
fun rememberNavigator(
    state: NavigationState,
    onEvent: NavigatorScope.(Any) -> Unit = {}
): NavigatorImpl = remember { NavigatorImpl(state = state, onEvent = onEvent) }

// ---- Multi stack ----

class MultiStackNavigationState(
    private val stacks: Map<NavKey, NavBackStack<NavKey>>,
    private val currentKeyState: MutableState<NavKey>
) {
    val currentKey: NavKey get() = currentKeyState.value
    val currentBackStack: NavBackStack<NavKey> get() = stacks.getValue(currentKey)

    fun selectTab(tab: NavKey) { currentKeyState.value = tab }
    fun backStack(tab: NavKey): NavBackStack<NavKey> = stacks.getValue(tab)
}

@Composable
fun rememberMultiStackNavigationState(
    stacks: Map<NavKey, NavBackStack<NavKey>>,
    startKey: NavKey,
): MultiStackNavigationState {
    val currentState = remember { mutableStateOf(startKey) }
    return remember(stacks) { MultiStackNavigationState(stacks, currentState) }
}

class MultiStackNavigatorImpl internal constructor(
    val state: MultiStackNavigationState,
    private val onEvent: NavigatorScope.(Any) -> Unit
) : Navigator, NavigatorScope {
    override fun push(destination: NavKey) { state.currentBackStack.push(destination) }
    override fun pop() { if (state.currentBackStack.size > 1) state.currentBackStack.pop() }
    override fun switchTab(tab: NavKey) { state.selectTab(tab) }

    override fun navigate(event: Any) { this.onEvent(event) }
    override fun navigateUp() { pop() }
}

@Composable
fun rememberMultiStackNavigator(
    state: MultiStackNavigationState,
    onEvent: NavigatorScope.(Any) -> Unit = {}
): MultiStackNavigatorImpl = remember { MultiStackNavigatorImpl(state = state, onEvent = onEvent) }
