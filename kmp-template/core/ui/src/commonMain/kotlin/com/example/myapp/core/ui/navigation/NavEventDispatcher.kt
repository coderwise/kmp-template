package com.example.myapp.core.ui.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf

/**
 * Holds the registered navigation event handlers and fans an event out to them.
 * Kept separate from [NavigationState] so back-stack management and event
 * dispatch can change independently (single responsibility).
 */
@Stable
class NavEventDispatcher {
    private val eventHandlers = mutableStateListOf<Navigator.(Any) -> Unit>()

    fun addEventHandler(handler: Navigator.(Any) -> Unit) {
        if (!eventHandlers.contains(handler)) {
            eventHandlers.add(handler)
        }
    }

    fun removeEventHandler(handler: Navigator.(Any) -> Unit) {
        eventHandlers.remove(handler)
    }

    fun dispatch(navigator: Navigator, event: Any) {
        eventHandlers.forEach { handler ->
            handler(navigator, event)
        }
    }
}
