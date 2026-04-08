package com.example.myapp.feature.weather.domain.repository

import com.example.myapp.core.domain.Result
import com.example.myapp.feature.weather.domain.model.Location
import com.example.myapp.feature.weather.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getWeather(latitude: Double, longitude: Double, city: String): Result<WeatherInfo>
    suspend fun searchLocations(query: String): Result<List<Location>>
}
