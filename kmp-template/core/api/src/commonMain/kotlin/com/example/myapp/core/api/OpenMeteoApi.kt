package com.example.myapp.core.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable

class OpenMeteoApi(
    private val httpClient: HttpClient
) {
    suspend fun getWeather(latitude: Double, longitude: Double): OpenMeteoResponse {
        return httpClient.get("https://api.open-meteo.com/v1/forecast") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("current_weather", true)
        }.body()
    }

    suspend fun searchLocations(query: String): GeocodingResponse {
        return httpClient.get("https://geocoding-api.open-meteo.com/v1/search") {
            parameter("name", query)
            parameter("count", 10)
            parameter("language", "en")
            parameter("format", "json")
        }.body()
    }
}

@Serializable
data class OpenMeteoResponse(
    val current_weather: CurrentWeather
)

@Serializable
data class CurrentWeather(
    val temperature: Double,
    val weathercode: Int
)

@Serializable
data class GeocodingResponse(
    val results: List<GeocodingResult>? = null
)

@Serializable
data class GeocodingResult(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null,
    val admin1: String? = null
)
