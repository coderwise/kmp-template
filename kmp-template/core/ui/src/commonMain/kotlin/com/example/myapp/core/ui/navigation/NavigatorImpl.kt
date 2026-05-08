package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.Composable
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
}

class NavigatorImpl internal constructor(
    val backStack: NavBackStack<NavKey>,
    private val onEvent: NavigatorScope.(Any) -> Unit
): Navigator, NavigatorScope {
    override fun push(destination: NavKey) { backStack.push(destination)}
    override fun pop() { if (backStack.size > 1) backStack.pop() }

    override fun dispatch(event: Any) { this.onEvent(event) }
}

@Composable
fun rememberNavigator(
    configuration: SavedStateConfiguration,
    startDestination: NavKey,
    onEvent: NavigatorScope.(Any) -> Unit = {}
): NavigatorImpl {
    val backStack = rememberNavBackStack(configuration, startDestination)

    return remember {
        NavigatorImpl(
            backStack = backStack,
            onEvent = onEvent
        )
    }
}
