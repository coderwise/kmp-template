package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.myapp.core.ui.util.pop
import com.example.myapp.core.ui.util.push

@Stable
class NavigationState : Navigator {
    private var _rootBackStack: NavBackStack<NavKey>? = null
    val rootBackStack: NavBackStack<NavKey>
        get() = _rootBackStack ?: error("NavigationState not initialized. Call onStart(startRoot) first.")

    val childBackStacks = mutableStateMapOf<NavKey, NavBackStack<NavKey>>()

    fun onStart(startRoot: NavKey) {
        if (_rootBackStack == null) {
            _rootBackStack = NavBackStack<NavKey>().apply { add(startRoot) }
        }
    }

    val currentRootKey: NavKey
        get() = rootBackStack.last()

    override val currentBackStack: NavBackStack<NavKey>
        get() = childBackStacks.getOrPut(currentRootKey) {
            NavBackStack()
        }

    override fun navigate(key: NavKey) {
        currentBackStack.push(key)
    }

    override fun switchRoot(key: NavKey) {
        if (currentRootKey == key) {
            while (currentBackStack.size > 1) {
                currentBackStack.pop()
            }
        } else {
            if (rootBackStack.contains(key)) {
                rootBackStack.remove(key)
            }
            rootBackStack.push(key)
        }
    }

    override fun navigateUp() {
        if (currentBackStack.size > 1) {
            currentBackStack.pop()
        } else if (rootBackStack.size > 1) {
            rootBackStack.pop()
        }
    }

    private var eventHandler: (Navigator.(Any) -> Unit)? = null

    fun setEventHandler(handler: Navigator.(Any) -> Unit) {
        this.eventHandler = handler
    }

    override fun onEvent(event: Any) {
        val handler = eventHandler
        if (handler != null) {
            handler(event)
        } else if (event is GlobalNavEvent.Back) {
            navigateUp()
        }
    }
}
