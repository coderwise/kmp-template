package com.example.myapp.core.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class OpenMeteoApi(
    private val httpClient: HttpClient
) {
    private companion object {
        const val FORECAST_URL = "https://api.open-meteo.com/v1/forecast"
        const val GEOCODING_URL = "https://geocoding-api.open-meteo.com/v1/search"
        const val REVERSE_GEOCODE_URL = "https://nominatim.openstreetmap.org/reverse"
        const val SEARCH_RESULT_LIMIT = 10
        const val USER_AGENT = "MyApp"
    }

    suspend fun getWeather(latitude: Double, longitude: Double): OpenMeteoResponse {
        return httpClient.get(FORECAST_URL) {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("current_weather", true)
        }.body()
    }

    suspend fun searchLocations(query: String): GeocodingResponse {
        return httpClient.get(GEOCODING_URL) {
            parameter("name", query)
            parameter("count", SEARCH_RESULT_LIMIT)
            parameter("language", "en")
            parameter("format", "json")
        }.body()
    }

    suspend fun reverseGeocode(latitude: Double, longitude: Double): NominatimResponse {
        return httpClient.get(REVERSE_GEOCODE_URL) {
            parameter("lat", latitude)
            parameter("lon", longitude)
            parameter("format", "json")
            header("User-Agent", USER_AGENT)
        }.body()
    }
}

@Serializable
data class OpenMeteoResponse(
    @SerialName("current_weather") val currentWeather: CurrentWeather
)

@Serializable
data class CurrentWeather(
    val temperature: Double,
    @SerialName("weathercode") val weatherCode: Int
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

@Serializable
data class NominatimResponse(
    @SerialName("display_name") val displayName: String,
    val name: String? = null,
    val address: NominatimAddress? = null
)

@Serializable
data class NominatimAddress(
    val city: String? = null,
    val town: String? = null,
    val village: String? = null,
    val hamlet: String? = null,
    val suburb: String? = null,
    val country: String? = null,
    val state: String? = null
)
