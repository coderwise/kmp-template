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
    fun navigateRoot(key: NavKey)
    fun replaceRoot(key: NavKey)
    fun switchRoot(key: NavKey)
    fun navigateUp()

    override fun onEvent(event: Any)
}
