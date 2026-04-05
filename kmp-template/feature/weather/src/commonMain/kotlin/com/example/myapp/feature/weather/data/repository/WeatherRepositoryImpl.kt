package com.example.myapp.feature.weather.data.repository

import com.example.myapp.core.domain.Result
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
    override suspend fun getWeather(city: String): Result<WeatherInfo> {
        return try {
            // Using Open-Meteo API (example: coordinates for London)
            // In a real app, you'd geocode the city name first.
            val response: OpenMeteoResponse = httpClient.get("https://api.open-meteo.com/v1/forecast") {
                parameter("latitude", 51.5074)
                parameter("longitude", -0.1278)
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
