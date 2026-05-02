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
                    city = city,
                    latitude = latitude,
                    longitude = longitude
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

    override suspend fun reverseGeocode(latitude: Double, longitude: Double): Result<Location> {
        return try {
            val response = api.reverseGeocode(latitude, longitude)
            val name = response.address?.let {
                it.city ?: it.town ?: it.village ?: it.hamlet ?: it.suburb ?: response.name
            } ?: response.display_name

            Result.Success(
                Location(
                    name = name,
                    latitude = latitude,
                    longitude = longitude,
                    country = response.address?.country,
                    admin1 = response.address?.state
                )
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
