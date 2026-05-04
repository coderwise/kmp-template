package com.example.myapp.core.ui.util

fun <T : Any> MutableList<T>.push(element: T) {
    add(element)
}

fun <T : Any> MutableList<T>.pop() {
    if (isNotEmpty()) {
        removeAt(size - 1)
    }
}
