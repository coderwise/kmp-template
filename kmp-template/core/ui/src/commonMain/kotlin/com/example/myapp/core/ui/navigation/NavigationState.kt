package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.myapp.core.ui.util.pop
import com.example.myapp.core.ui.util.push

@Stable
class NavigationState(
    val rootBackStack: NavBackStack<NavKey>,
    initialChildBackStacks: Map<NavKey, NavBackStack<NavKey>> = emptyMap()
) {
    val childBackStacks = mutableStateMapOf<NavKey, NavBackStack<NavKey>>().apply {
        putAll(initialChildBackStacks)
    }

    val currentRootKey: NavKey
        get() = rootBackStack.last()

    val currentBackStack: NavBackStack<NavKey>
        get() = childBackStacks.getOrPut(currentRootKey) {
            NavBackStack(currentRootKey)
        }

    fun push(key: NavKey) {
        currentBackStack.push(key)
    }

    fun pop() {
        if (currentBackStack.size > 1) {
            currentBackStack.pop()
        } else if (rootBackStack.size > 1) {
            rootBackStack.pop()
        }
    }

    fun switchRoot(key: NavKey) {
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

    companion object {
        fun Saver(): Saver<NavigationState, *> = listSaver(
            save = { state ->
                listOf(
                    state.rootBackStack.toList(),
                    state.childBackStacks.mapValues { it.value.toList() }
                )
            },
            restore = { saved ->
                @Suppress("UNCHECKED_CAST")
                val rootList = saved[0] as List<NavKey>
                @Suppress("UNCHECKED_CAST")
                val childrenMap = saved[1] as Map<NavKey, List<NavKey>>
                
                val rootBackStack = NavBackStack<NavKey>().apply {
                    rootList.forEach { add(it) }
                }
                
                val childrenBackStacks = childrenMap.mapValues { (_, list) ->
                    NavBackStack<NavKey>().apply {
                        list.forEach { add(it) }
                    }
                }

                NavigationState(
                    rootBackStack = rootBackStack,
                    initialChildBackStacks = childrenBackStacks
                )
            }
        )
    }
}

@Composable
fun rememberNavigationState(startRoot: NavKey): NavigationState {
    return rememberSaveable(saver = NavigationState.Saver()) {
        val rootBackStack = NavBackStack<NavKey>().apply { add(startRoot) }
        NavigationState(rootBackStack)
    }
}
