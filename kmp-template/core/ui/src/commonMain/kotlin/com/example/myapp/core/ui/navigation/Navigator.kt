package com.example.myapp.core.ui.navigation

interface Navigator<T> {
    fun back()
    fun onEvent(event: T) {}
}
