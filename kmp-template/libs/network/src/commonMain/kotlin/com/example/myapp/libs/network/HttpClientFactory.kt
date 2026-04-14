package com.example.myapp.libs.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun createHttpClient(): HttpClient

internal fun HttpClientConfig<*>.configure(
    host: String = "localhost",
    port: Int = 8080,
    logger: Logger = Logger.DEFAULT
) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(Logging) {
        this.logger = logger
        level = LogLevel.BODY
    }
    install(Resources)
    defaultRequest {
        url {
            protocol = URLProtocol.HTTP
            this.host = host
            this.port = port
        }
    }
}
