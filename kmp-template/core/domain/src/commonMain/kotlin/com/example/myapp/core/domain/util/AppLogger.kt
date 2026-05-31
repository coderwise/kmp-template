package com.example.myapp.core.domain.util

/**
 * Minimal multiplatform logging entry point. Centralizes how the app records
 * diagnostics so call sites never silently swallow failures. Swap the
 * implementation here (Kermit, Napier, platform loggers) without touching callers.
 */
object AppLogger {
    fun warn(tag: String, message: String, throwable: Throwable? = null) {
        println("W/$tag: $message${throwable?.let { "\n${it.stackTraceToString()}" }.orEmpty()}")
    }

    fun error(tag: String, message: String, throwable: Throwable? = null) {
        println("E/$tag: $message${throwable?.let { "\n${it.stackTraceToString()}" }.orEmpty()}")
    }
}
