package com.example.myapp.feature.weather.data.repository

import com.example.myapp.core.domain.model.Result
import com.example.myapp.core.api.OpenMeteoApi
import com.example.myapp.core.domain.model.Location
import com.example.myapp.feature.weather.domain.model.WeatherInfo
import com.example.myapp.feature.weather.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val api: OpenMeteoApi
) : WeatherRepository {
    override suspend fun getWeather(latitude: Double, longitude: Double, city: String): Result<WeatherInfo> {
        return try {
            val response = api.getWeather(latitude, longitude)

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
            val response = api.searchLocations(query)

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
