package com.example.myapp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.logging.Logger
import android.util.Log

actual fun createHttpClient(): HttpClient = HttpClient(Android) {
    configure(object : Logger {
        override fun log(message: String) {
            Log.d("HttpClient", message)
        }
    })
}
