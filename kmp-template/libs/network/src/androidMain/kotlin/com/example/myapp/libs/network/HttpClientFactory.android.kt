package com.example.myapp.libs.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.logging.Logger
import android.util.Log

actual fun createHttpClient(): HttpClient = HttpClient(Android) {
    configure(
        host = "10.0.2.2",
        port = 8080,
        logger = object : Logger {
            override fun log(message: String) {
                Log.d("HttpClient", message)
            }
        }
    )
}
