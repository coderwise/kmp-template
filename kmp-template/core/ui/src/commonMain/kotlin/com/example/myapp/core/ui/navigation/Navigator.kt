package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

sealed interface GlobalNavEvent {
    data object Back : GlobalNavEvent
}

fun interface NavEventHandler {
    fun onEvent(event: Any)
}

@Stable
interface Navigator : NavEventHandler {
    val currentBackStack: NavBackStack<NavKey>
    fun navigate(key: NavKey)
    fun push(key: NavKey) = navigate(key)
    fun switchRoot(key: NavKey)
    fun navigateUp()
    fun pop()
    
    override fun onEvent(event: Any)
}
