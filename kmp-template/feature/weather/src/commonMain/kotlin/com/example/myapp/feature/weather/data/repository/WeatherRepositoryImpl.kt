package com.example.myapp.feature.weather.data.repository

import com.example.myapp.core.domain.Result
import com.example.myapp.feature.weather.domain.model.Location
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.feature.weather.domain.repository.WeatherRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable

class WeatherRepositoryImpl(
    private val httpClient: HttpClient
) : WeatherRepository {
    override suspend fun getWeather(latitude: Double, longitude: Double, city: String): Result<WeatherInfo> {
        return try {
            val response: OpenMeteoResponse = httpClient.get("https://api.open-meteo.com/v1/forecast") {
                parameter("latitude", latitude)
                parameter("longitude", longitude)
                parameter("current_weather", true)
            }.body()

            Result.Success(
                WeatherInfo(
                    temperature = response.current_weather.temperature,
                    condition = "Clear", // Simplified for this example
                    city = city
                )
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun searchLocations(query: String): Result<List<Location>> {
        return try {
            val response: GeocodingResponse = httpClient.get("https://geocoding-api.open-meteo.com/v1/search") {
                parameter("name", query)
                parameter("count", 10)
                parameter("language", "en")
                parameter("format", "json")
            }.body()

            Result.Success(
                response.results?.map {
                    Location(
                        name = it.name,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        country = it.country,
                        admin1 = it.admin1
                    )
                } ?: emptyList()
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
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
