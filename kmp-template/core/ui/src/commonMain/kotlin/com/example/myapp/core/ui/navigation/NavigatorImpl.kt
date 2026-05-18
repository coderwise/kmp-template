package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.myapp.core.ui.util.pop
import com.example.myapp.core.ui.util.push

@Stable
internal class NavBridge : ViewModel(), Navigator {
    var target by mutableStateOf<Navigator?>(null)

    override val currentBackStack: NavBackStack<NavKey>
        get() = target?.currentBackStack ?: NavBackStack()

    override fun navigate(key: NavKey) {
        target?.navigate(key)
    }

    override fun switchRoot(key: NavKey) {
        target?.switchRoot(key)
    }

    override fun navigateUp() {
        target?.navigateUp()
    }

    override fun pop() {
        target?.pop()
    }

    override fun onEvent(event: Any) {
        target?.onEvent(event)
    }
}

@Stable
private class ActionNavigator(
    override val currentBackStack: NavBackStack<NavKey>,
    private val onNavigate: (NavKey) -> Unit,
    private val onSwitchRoot: (NavKey) -> Unit = {},
    private val onPop: () -> Unit,
    private val eventHandler: Navigator.(Any) -> Unit
) : Navigator {
    override fun navigate(key: NavKey) = onNavigate(key)
    override fun switchRoot(key: NavKey) = onSwitchRoot(key)
    override fun onEvent(event: Any) = eventHandler(event)
    override fun navigateUp() = onEvent(GlobalNavEvent.Back)
    override fun pop() = onPop()
}

@Composable
fun rememberNavigator(
    startDestination: NavKey,
    configuration: SavedStateConfiguration,
    onEvent: Navigator.(Any) -> Unit = {}
): Navigator {
    val backStack = rememberNavBackStack(configuration, startDestination)
    val currentOnEvent by rememberUpdatedState(onEvent)
    
    val bridge = viewModel { NavBridge() }
    
    val activeNavigator = remember(backStack) { 
        ActionNavigator(
            currentBackStack = backStack,
            onNavigate = { backStack.push(it) },
            onPop = { if (backStack.size > 1) backStack.pop() },
            eventHandler = { event -> currentOnEvent(event) }
        )
    }
    
    bridge.target = activeNavigator
    return bridge
}

@Composable
fun rememberAppNavigator(
    startRoot: NavKey,
    onEvent: Navigator.(Any) -> Unit = {}
): Navigator {
    val state = rememberNavigationState(startRoot)
    val currentOnEvent by rememberUpdatedState(onEvent)
    
    val bridge = viewModel { NavBridge() }
    
    val activeNavigator = remember(state) {
        ActionNavigator(
            currentBackStack = state.currentBackStack,
            onNavigate = { state.push(it) },
            onSwitchRoot = { state.switchRoot(it) },
            onPop = { state.pop() },
            eventHandler = { event -> currentOnEvent(event) }
        )
    }
    
    bridge.target = activeNavigator
    return bridge
}
