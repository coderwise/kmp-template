package com.example.myapp.feature.weather.domain.repository

import com.example.myapp.core.domain.model.Result
import com.example.myapp.core.domain.model.Location
import com.example.myapp.feature.weather.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getWeather(latitude: Double, longitude: Double, city: String): Result<WeatherInfo>
    suspend fun searchLocations(query: String): Result<List<Location>>
    suspend fun reverseGeocode(latitude: Double, longitude: Double): Result<Location>
}
