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

    fun getBackStack(rootKey: NavKey): NavBackStack<NavKey> {
        return childBackStacks.getOrPut(rootKey) {
            NavBackStack()
        }
    }

    fun onStart(startRoot: NavKey) {
        if (_rootBackStack == null) {
            _rootBackStack = NavBackStack<NavKey>().apply { add(startRoot) }
        }
    }

    val currentRootKey: NavKey
        get() = rootBackStack.last()

    override val currentBackStack: NavBackStack<NavKey>
        get() = getBackStack(currentRootKey)

    override fun navigate(key: NavKey) {
        currentBackStack.push(key)
    }

    override fun navigateRoot(key: NavKey) {
        rootBackStack.push(key)
    }

    override fun replaceRoot(key: NavKey) {
        rootBackStack.clear()
        rootBackStack.push(key)
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
        navigateUp(currentRootKey)
    }

    fun navigateUp(rootKey: NavKey) {
        val backStack = getBackStack(rootKey)
        if (backStack.size > 1) {
            backStack.pop()
        } else if (rootBackStack.size > 1) {
            rootBackStack.pop()
        }
    }

    private val eventHandlers = mutableStateListOf<Navigator.(Any) -> Unit>()

    fun addEventHandler(handler: Navigator.(Any) -> Unit) {
        if (!eventHandlers.contains(handler)) {
            eventHandlers.add(handler)
        }
    }

    fun removeEventHandler(handler: Navigator.(Any) -> Unit) {
        eventHandlers.remove(handler)
    }

    override fun onEvent(event: Any) {
        if (event is GlobalNavEvent.Back) {
            navigateUp()
            return
        }
        // Iterate backwards to let more specific (later added) handlers take precedence if needed,
        // but here we just call all of them. 
        // Note: We might want a way to "consume" events, but for now we just call all.
        eventHandlers.forEach { handler ->
            handler(this, event)
        }
    }
}
